#!/bin/sh
# Regenerates src/main/java/com/docuseal from the DocuSeal OpenAPI spec
# using Fern (runs the generator locally in Docker).
# Usage: ./generate-types.sh [path-or-url-to-openapi-json]
set -e

cd "$(dirname "$0")"

SPEC="${1:-https://console.docuseal.com/openapi.yml?format=json}"

case "$SPEC" in
  http*) curl -sf "$SPEC" -o openapi.tmp.json ;;
  *) cp "$SPEC" openapi.tmp.json ;;
esac

# Drop webhook payload schemas (the SDK exposes only the REST client) and
# swap the legacy POST /submissions for the newer /submissions/init, which
# is not in the public spec yet: same request body, envelope response.
ruby -rjson -e '
  path = "openapi.tmp.json"
  spec = JSON.parse(File.read(path))
  spec.delete("webhooks")

  # No tags -> Fern puts every method directly on the root client
  # (client.getTemplate(id)) instead of resource groups (client.templates()).
  spec.delete("tags")
  spec["paths"].each_value do |methods|
    methods.each_value do |op|
      next unless op.is_a?(Hash)

      op.delete("tags")

      # Name the generated method-input wrappers <OperationId>Params:
      # "Request" is reserved for spec body components.
      params_name = op["operationId"].sub(/\A./) { |c| c.upcase } + "Params"
      op["x-fern-request-name"] = params_name
      op["x-fern-sdk-request-name"] = params_name
    end
  end

  init = spec["paths"]["/submissions"].delete("post")
  init["responses"]["200"]["content"]["application/json"].delete("example")
  init["responses"]["200"]["content"]["application/json"]["schema"] = {
    "type" => "object",
    "required" => %w[id submitters expired_at created_at],
    "properties" => {
      "id" => { "type" => "integer", "description" => "Submission unique ID number." },
      "submitters" => { "$ref" => "#/components/schemas/CreateSubmissionsFromEmailsResponse" },
      "expired_at" => { "type" => %w[string null], "description" => "The date and time when the submission expires." },
      "created_at" => { "type" => "string", "description" => "The date and time when the submission was created." }
    }
  }
  spec["paths"]["/submissions/init"] = { "post" => init }

  File.write(path, JSON.generate(spec))
'

rm -rf .fern-out
CI=true npx -y fern-api@5.67.1 generate --local

rm -rf src/main/java/com/docuseal
mkdir -p src/main/java/com/docuseal
cp -r .fern-out/. src/main/java/com/docuseal/
rm -f src/main/java/com/docuseal/core/*Test.java
rm -f src/main/java/com/docuseal/README.md

rm -rf .fern-out openapi.tmp.json
