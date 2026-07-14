#!/usr/bin/env ruby
# frozen_string_literal: true

# Regenerates src/main/java/com/docuseal from the DocuSeal OpenAPI spec
# using Fern (runs the generator locally in Docker).
# Usage: ./generate-types.rb [path-or-url-to-openapi-json]

require 'fileutils'

Dir.chdir(__dir__)

# A local file argument must be an SDK-mode dump: ApiSpec.call(sdk: true).
spec = ARGV[0] || 'https://console.docuseal.com/openapi.json?sdk=true'

if spec.start_with?('http')
  system('curl', '-sf', spec, '-o', 'openapi.tmp.json', exception: true)
else
  FileUtils.cp(spec, 'openapi.tmp.json')
end

FileUtils.rm_rf('.fern-out')
system({ 'CI' => 'true' }, 'npx', '-y', 'fern-api@5.67.1', 'generate', '--local', exception: true)

FileUtils.rm_rf('src/main/java/com/docuseal')
FileUtils.mkdir_p('src/main/java/com/docuseal')
FileUtils.cp_r('.fern-out/.', 'src/main/java/com/docuseal')
FileUtils.rm_f(Dir.glob('src/main/java/com/docuseal/core/*Test.java'))
FileUtils.rm_f('src/main/java/com/docuseal/README.md')

Dir.glob('patches/*.patch').sort.each do |patch|
  system('git', 'apply', patch, exception: true)
end

FileUtils.rm_rf('.fern-out')
FileUtils.rm_f('openapi.tmp.json')
