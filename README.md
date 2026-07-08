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

Requires Java 17+. Dependencies: Jackson, jakarta.annotation-api.

## Configuration

Get your API key at [console.docuseal.com/api](https://console.docuseal.com/api).

### Global cloud (docuseal.com)

```java
DocusealClient client = new DocusealClient(System.getenv("DOCUSEAL_API_KEY"));
```

### EU cloud (docuseal.eu)

```java
DocusealClient client = new DocusealClient(System.getenv("DOCUSEAL_API_KEY"), DocusealClient.EU_URL);
```

### On-premises

```java
DocusealClient client = new DocusealClient(System.getenv("DOCUSEAL_API_KEY"), "https://yourdocuseal.com/api");
```

## Usage

### List templates

```java
GetTemplatesResponse templates = client.listTemplates(new ListTemplatesParams().limit(20));

for (GetTemplatesResponseDataInner template : templates.getData()) {
  System.out.println(template.getId() + " " + template.getName());
}
```

### Create a signature request

```java
CreateSubmissionRequest data = new CreateSubmissionRequest()
    .templateId(1000001)
    .addSubmittersItem(new CreateSubmissionRequestSubmittersInner()
        .role("First Party")
        .email("signer@example.com"));

CreateSubmissionResponse submission = client.createSubmission(data);

System.out.println(submission.getSubmitters().get(0).getEmbedSrc());
```

### Track a submission

```java
GetSubmissionResponse submission = client.getSubmission(1001);

System.out.println(submission.getStatus());

for (CompletedDocument document : submission.getDocuments()) {
  System.out.println(document.getName() + " " + document.getUrl());
}
```

### Handle errors

```java
try {
  client.getTemplate(42);
} catch (DocusealException e) {
  System.out.println(e.getStatusCode() + " " + e.getMessage());
}
```

## Regenerating models

`com.docuseal.models` is generated from the DocuSeal OpenAPI specification
and is never edited by hand:

```sh
./generate-types.sh
```

Requires Node.js (`npx`), Java and `ruby`.

## Documentation

Full API documentation: [docuseal.com/docs/api](https://www.docuseal.com/docs/api)

## License

MIT
