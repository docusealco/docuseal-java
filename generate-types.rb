#!/usr/bin/env ruby
# frozen_string_literal: true

# Regenerates src/main/java/com/docuseal from the DocuSeal OpenAPI spec
# using Fern (runs the generator locally in Docker).
# Usage: ./generate-types.rb [path-or-url-to-openapi-json]

require 'fileutils'

Dir.chdir(__dir__)

# A local file argument must be an SDK-mode dump: ApiSpec.call(sdk: true).
spec = ARGV[0] || 'https://console.docuseal.com/openapi.yml?format=json&sdk=true'

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

# DELETE /<resource>/{id}?permanently=true has no own OpenAPI operation.

RESOURCES = [
  {
    entity: 'Template',
    path: 'templates',
    result: 'TemplateArchiveResult',
    description: 'The API endpoint allows you to permanently delete a document template and all of its submissions.'
  },
  {
    entity: 'Submission',
    path: 'submissions',
    result: 'SubmissionArchiveResult',
    description: 'The API endpoint allows you to permanently delete a submission and all of its submitters and documents.'
  }
].freeze

def append_methods(filename, methods)
  path = File.join('src/main/java/com/docuseal', filename)
  content = File.read(path)
  raise "#{filename}: permanently delete methods already present" if content.include?('permanentlyDelete')

  File.write(path, content.sub(/\}\s*\z/) { "#{methods}}\n" })
end

append_methods('DocusealClient.java', RESOURCES.map do |res|
  <<~JAVA.gsub(/^(?!$)/, '  ')
    /**
     * #{res[:description]}
     */
    public #{res[:result]} permanentlyDelete#{res[:entity]}(int id) {
      return this.rawClient.permanentlyDelete#{res[:entity]}(id).body();
    }

    /**
     * #{res[:description]}
     */
    public #{res[:result]} permanentlyDelete#{res[:entity]}(int id, RequestOptions requestOptions) {
      return this.rawClient.permanentlyDelete#{res[:entity]}(id, requestOptions).body();
    }
  JAVA
end.join("\n"))

append_methods('AsyncDocusealClient.java', RESOURCES.map do |res|
  <<~JAVA.gsub(/^(?!$)/, '  ')
    /**
     * #{res[:description]}
     */
    public CompletableFuture<#{res[:result]}> permanentlyDelete#{res[:entity]}(int id) {
      return this.rawClient.permanentlyDelete#{res[:entity]}(id).thenApply(response -> response.body());
    }

    /**
     * #{res[:description]}
     */
    public CompletableFuture<#{res[:result]}> permanentlyDelete#{res[:entity]}(int id,
        RequestOptions requestOptions) {
      return this.rawClient.permanentlyDelete#{res[:entity]}(id, requestOptions).thenApply(response -> response.body());
    }
  JAVA
end.join("\n"))

append_methods('RawDocusealClient.java', RESOURCES.map do |res|
  <<~JAVA.gsub(/^(?!$)/, '    ')
    /**
     * #{res[:description]}
     */
    public DocusealClientHttpResponse<#{res[:result]}> permanentlyDelete#{res[:entity]}(int id) {
      return permanentlyDelete#{res[:entity]}(id, null);
    }

    /**
     * #{res[:description]}
     */
    public DocusealClientHttpResponse<#{res[:result]}> permanentlyDelete#{res[:entity]}(int id,
        RequestOptions requestOptions) {
      HttpUrl httpUrl = HttpUrl.parse(this.clientOptions.environment().getUrl()).newBuilder()
        .addPathSegments("#{res[:path]}")
        .addPathSegment(Integer.toString(id))
        .addQueryParameter("permanently", "true")
        .build();
      Request.Builder _requestBuilder = new Request.Builder()
        .url(httpUrl)
        .method("DELETE", null)
        .headers(Headers.of(clientOptions.headers(requestOptions)))
        .addHeader("Accept", "application/json");
      Request okhttpRequest = _requestBuilder.build();
      OkHttpClient client = clientOptions.httpClient();
      if (requestOptions != null && requestOptions.getTimeout().isPresent()) {
        client = clientOptions.httpClientWithTimeout(requestOptions);
      }
      try (Response response = client.newCall(okhttpRequest).execute()) {
        ResponseBody responseBody = response.body();
        if (response.isSuccessful()) {
          return new DocusealClientHttpResponse<>(ObjectMappers.JSON_MAPPER.readValue(responseBody.string(), #{res[:result]}.class), response);
        }
        String responseBodyString = responseBody != null ? responseBody.string() : "{}";
        throw new DocusealClientApiException("Error with status code " + response.code(), response.code(), ObjectMappers.JSON_MAPPER.readValue(responseBodyString, Object.class), response);
      }
      catch (IOException e) {
        throw new DocusealClientException("Network error executing HTTP request", e);
      }
    }
  JAVA
end.join("\n"))

append_methods('AsyncRawDocusealClient.java', RESOURCES.map do |res|
  <<~JAVA.gsub(/^(?!$)/, '    ')
    /**
     * #{res[:description]}
     */
    public CompletableFuture<DocusealClientHttpResponse<#{res[:result]}>> permanentlyDelete#{res[:entity]}(
        int id) {
      return permanentlyDelete#{res[:entity]}(id, null);
    }

    /**
     * #{res[:description]}
     */
    public CompletableFuture<DocusealClientHttpResponse<#{res[:result]}>> permanentlyDelete#{res[:entity]}(
        int id, RequestOptions requestOptions) {
      HttpUrl httpUrl = HttpUrl.parse(this.clientOptions.environment().getUrl()).newBuilder()
        .addPathSegments("#{res[:path]}")
        .addPathSegment(Integer.toString(id))
        .addQueryParameter("permanently", "true")
        .build();
      Request.Builder _requestBuilder = new Request.Builder()
        .url(httpUrl)
        .method("DELETE", null)
        .headers(Headers.of(clientOptions.headers(requestOptions)))
        .addHeader("Accept", "application/json");
      Request okhttpRequest = _requestBuilder.build();
      OkHttpClient client = clientOptions.httpClient();
      if (requestOptions != null && requestOptions.getTimeout().isPresent()) {
        client = clientOptions.httpClientWithTimeout(requestOptions);
      }
      CompletableFuture<DocusealClientHttpResponse<#{res[:result]}>> future = new CompletableFuture<>();
      client.newCall(okhttpRequest).enqueue(new Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
          try (ResponseBody responseBody = response.body()) {
            if (response.isSuccessful()) {
              future.complete(new DocusealClientHttpResponse<>(ObjectMappers.JSON_MAPPER.readValue(responseBody.string(), #{res[:result]}.class), response));
              return;
            }
            String responseBodyString = responseBody != null ? responseBody.string() : "{}";
            future.completeExceptionally(new DocusealClientApiException("Error with status code " + response.code(), response.code(), ObjectMappers.JSON_MAPPER.readValue(responseBodyString, Object.class), response));
            return;
          }
          catch (IOException e) {
            future.completeExceptionally(new DocusealClientException("Network error executing HTTP request", e));
          }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
          future.completeExceptionally(new DocusealClientException("Network error executing HTTP request", e));
        }
      });
      return future;
    }
  JAVA
end.join("\n"))

FileUtils.rm_rf('.fern-out')
FileUtils.rm_f('openapi.tmp.json')
