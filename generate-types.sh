#!/bin/sh
# Regenerates src/main/java/com/docuseal from the DocuSeal OpenAPI spec
# using Fern (runs the generator locally in Docker).
# Usage: ./generate-types.sh [path-or-url-to-openapi-json]
set -e

cd "$(dirname "$0")"

# The spec is served in SDK mode (?sdk=true): no webhooks/tags, "Create
# Submission" built from POST /submissions/init, <OperationId>Params request
# names. A local file argument must be an SDK-mode dump: ApiSpec.call(sdk: true).
SPEC="${1:-https://console.docuseal.com/openapi.yml?format=json&sdk=true}"

case "$SPEC" in
  http*) curl -sf "$SPEC" -o openapi.tmp.json ;;
  *) cp "$SPEC" openapi.tmp.json ;;
esac


rm -rf .fern-out
CI=true npx -y fern-api@5.67.1 generate --local

rm -rf src/main/java/com/docuseal
mkdir -p src/main/java/com/docuseal
cp -r .fern-out/. src/main/java/com/docuseal/
rm -f src/main/java/com/docuseal/core/*Test.java
rm -f src/main/java/com/docuseal/README.md

rm -rf .fern-out openapi.tmp.json
