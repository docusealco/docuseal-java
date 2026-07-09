# DocuSeal Java

Java client for the [DocuSeal API](https://www.docuseal.com/docs/api). DocuSeal is an open source platform to create, fill and sign digital documents.

## Installation

```xml
<dependency>
  <groupId>com.docuseal</groupId>
  <artifactId>docuseal-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

Requires Java 17+.

## Configuration

Get your API key at [console.docuseal.com/api](https://console.docuseal.com/api).

### Global cloud (docuseal.com)

```java
DocusealClient client = DocusealClient.builder()
    .apiKey(System.getenv("DOCUSEAL_API_KEY"))
    .build();
```

### EU cloud (docuseal.eu)

```java
DocusealClient client = DocusealClient.builder()
    .apiKey(System.getenv("DOCUSEAL_API_KEY"))
    .url("https://api.docuseal.eu")
    .build();
```

### On-premises

```java
DocusealClient client = DocusealClient.builder()
    .apiKey(System.getenv("DOCUSEAL_API_KEY"))
    .url("https://yourdocuseal.com/api")
    .build();
```

## Usage

### List templates

```java
GetTemplatesResponse templates = client.getTemplates(
    GetTemplatesParams.builder().limit(20).build());

for (var template : templates.getData()) {
  System.out.println(template.getId() + " " + template.getName());
}
```

### Create a signature request

Builders are staged: required fields must be provided before `build()`
compiles.

```java
CreateSubmissionResponse submission = client.createSubmission(
    CreateSubmissionParams.builder()
        .templateId(1000001)
        .submitters(List.of(
            CreateSubmissionRequestSubmitter.builder()
                .role("First Party")
                .email("signer@example.com")
                .build()))
        .build());

System.out.println(submission.getSubmitters().get(0).getEmbedSrc());
```

### Track a submission

```java
GetSubmissionResponse submission = client.getSubmission(1001);

System.out.println(submission.getStatus());

for (var document : submission.getDocuments()) {
  System.out.println(document.getName() + " " + document.getUrl());
}
```

### Async client

```java
AsyncDocusealClient client = AsyncDocusealClient.builder()
    .apiKey(System.getenv("DOCUSEAL_API_KEY"))
    .build();

CompletableFuture<GetTemplatesResponse> templates = client.getTemplates();
```

### Handle errors

```java
try {
  client.getTemplate(42);
} catch (DocusealClientApiException e) {
  System.out.println(e.statusCode() + " " + e.getMessage());
}
```

## Regenerating the SDK

`src/main/java/com/docuseal` is generated from the DocuSeal OpenAPI
specification by [Fern](https://buildwithfern.com) and is never edited by
hand:

```sh
./generate-types.sh
```

Requires Node.js (`npx`), Docker and `ruby`.

## Documentation

Full API documentation: [docuseal.com/docs/api](https://www.docuseal.com/docs/api)

## License

MIT
