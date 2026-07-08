#!/bin/sh
# Regenerates src/main/java/com/docuseal/models from the DocuSeal OpenAPI spec.
# Usage: ./scripts/generate-types.sh [path-or-url-to-openapi-json]
set -e

cd "$(dirname "$0")"

SPEC="${1:-https://console.docuseal.com/openapi.yml?format=json}"
TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

case "$SPEC" in
  http*) curl -sf "$SPEC" -o "$TMP_DIR/openapi.json" ;;
  *) cp "$SPEC" "$TMP_DIR/openapi.json" ;;
esac

# The SDK exposes only the REST API client: drop webhook payload schemas.
ruby -rjson -e '
  path = ARGV[0]
  spec = JSON.parse(File.read(path))
  spec.delete("webhooks")
  File.write(path, JSON.generate(spec))
' "$TMP_DIR/openapi.json"

npx -y @openapitools/openapi-generator-cli generate -g java \
  -i "$TMP_DIR/openapi.json" -o "$TMP_DIR/out" \
  --additional-properties library=native,openApiNullable=false,useJakartaEe=true,invokerPackage=com.docuseal,modelPackage=com.docuseal.models,apiPackage=com.docuseal.api,hideGenerationTimestamp=true \
  --global-property models,supportingFiles,modelDocs=false,modelTests=false \
  --skip-validate-spec > /dev/null

rm -rf src/main/java/com/docuseal/models
mkdir -p src/main/java/com/docuseal/models
cp "$TMP_DIR"/out/src/main/java/com/docuseal/models/*.java src/main/java/com/docuseal/models/
cp "$TMP_DIR"/out/src/main/java/com/docuseal/JSON.java \
   "$TMP_DIR"/out/src/main/java/com/docuseal/RFC3339DateFormat.java \
   "$TMP_DIR"/out/src/main/java/com/docuseal/RFC3339InstantDeserializer.java \
   "$TMP_DIR"/out/src/main/java/com/docuseal/RFC3339JavaTimeModule.java \
   src/main/java/com/docuseal/

MODELS=src/main/java/com/docuseal/models/*.java

# Drop toUrlQueryString helpers: they are the only thing pulling the generated
# ApiClient into models, which this SDK replaces with a hand-written client.
perl -0pi -e 's{  /\*\*\n   \* Convert the instance into URL query string.*?return joiner\.toString\(\);\n  \}\n}{}s' $MODELS
perl -0pi -e 's{  /\*\*\n   \* Convert the instance into URL query string.*?\n    return null;\n  \}\n}{}s' $MODELS
perl -0pi -e 's{^import com\.docuseal\.ApiClient;\n}{}m' $MODELS

# openapi-generator bugs for oneOf schemas with an array variant:
# method names with generics and .class on parameterized types.
perl -pi -e 's/getList<(\w+)>\(\)/get\1List()/g' $MODELS
perl -pi -e 's/List<\w+>\.class/List.class/g' $MODELS

# Invalid Java for `default: false` on the oneOf `mask` field preference.
perl -pi -e 's/(private \w*PreferencesMask mask) = false;/$1;/' $MODELS
