# DocuSeal Java

The DocuSeal Java library provides seamless integration with the DocuSeal API, allowing developers to interact with DocuSeal's electronic signature and document management features directly within Java applications. This library is designed to simplify API interactions and provide tools for efficient implementation.

## Documentation

Detailed documentation is available at [DocuSeal API Docs](https://www.docuseal.com/docs/api?lang=java).

## Installation

To install the Java SDK, add the following dependency to your project:

Gradle

```groovy
implementation 'com.docuseal:docuseal-java:+'
```

Maven

```xml
<dependency>
  <groupId>com.docuseal</groupId>
  <artifactId>docuseal-java</artifactId>
  <version>LATEST</version>
</dependency>
```

Requires Java 17+.

## Usage

### Configuration

Set up the library with your DocuSeal API key based on your deployment. Retrieve your API key from the appropriate location:

#### Global Cloud

API keys for the global cloud can be obtained from your [Global DocuSeal Console](https://console.docuseal.com/api).

```java
var client = new DocusealClient(System.getenv("DOCUSEAL_API_KEY"));
```

#### EU Cloud

API keys for the EU cloud can be obtained from your [EU DocuSeal Console](https://console.docuseal.eu/api).

```java
import com.docuseal.core.Environment;

var client = new DocusealClient(System.getenv("DOCUSEAL_API_KEY"), Environment.EU);
```

#### On-Premises

For on-premises installations, API keys can be retrieved from the API settings page of your deployed application, e.g., https://yourdocusealapp.com/settings/api.

```java
var client = new DocusealClient(System.getenv("DOCUSEAL_API_KEY"), "https://yourdocusealapp.com/api");
```

## API Methods

### getSubmissions(params)

[Documentation](https://www.docuseal.com/docs/api?lang=java#list-all-submissions)

Provides the ability to retrieve a list of available submissions.


```java
var submissions = client.getSubmissions(GetSubmissionsParams.builder().limit(10).build());
```

### getSubmission(id)

[Documentation](https://www.docuseal.com/docs/api?lang=java#get-a-submission)

Provides the functionality to retrieve information about a submission.


```java
var submission = client.getSubmission(1001);
```

### getSubmissionDocuments(id)

[Documentation](https://www.docuseal.com/docs/api?lang=java#get-submission-documents)

This endpoint returns a list of partially filled documents for a submission. If the submission has been completed, the final signed documents are returned.


```java
var submission = client.getSubmissionDocuments(1001);
```

### createSubmission(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#create-a-submission)

This API endpoint allows you to create signature requests (submissions) for a document template and send them to the specified submitters (signers).

**Related Guides:**<br>
[Send documents for signature via API](https://www.docuseal.com/guides/send-documents-for-signature-via-api)
[Pre-fill PDF document form fields with API](https://www.docuseal.com/guides/pre-fill-pdf-document-form-fields-with-api)


```java
var submission = client.createSubmission(CreateSubmissionParams.builder()
    .templateId(1000001)
    .submitters(List.of(
      CreateSubmissionSubmitterParams.builder()
        .role("First Party")
        .email("john.doe@example.com")
        .build()))
    .sendEmail(true)
    .build());
```

### createSubmissionFromPdf(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#create-a-submission-from-pdf)

Provides the functionality to create one-off submission request from a PDF. Use `{{Field Name;role=Signer1;type=date}}` text tags to define fillable fields in the document. See [https://www.docuseal.com/examples/fieldtags.pdf](https://www.docuseal.com/examples/fieldtags.pdf) for more text tag formats. Or specify the exact pixel coordinates of the document fields using `fields` param.

**Related Guides:**<br>
[Use embedded text field tags to create a fillable form](https://www.docuseal.com/guides/use-embedded-text-field-tags-in-the-pdf-to-create-a-fillable-form)


```java
var submission = client.createSubmissionFromPdf(CreateSubmissionFromPdfParams.builder()
    .documents(List.of(
      CreateSubmissionFromPdfDocumentParams.builder()
        .name("string")
        .file("base64")
        .fields(List.of(
          CreateSubmissionDocumentFieldParams.builder()
            .name("string")
            .areas(List.of(
              CreateSubmissionDocumentFieldAreaParams.builder()
                .x(0)
                .y(0)
                .w(0)
                .h(0)
                .page(1)
                .build()))
            .build()))
        .build()))
    .submitters(List.of(
      CreateSubmissionSubmitterParams.builder()
        .role("First Party")
        .email("john.doe@example.com")
        .build()))
    .name("Test Submission Document")
    .build());
```

### createSubmissionFromDocx(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#create-a-submission-from-docx)

Provides functionality to create a one-off submission request from a DOCX file with dynamic content variables. Use `[[variable_name]]` text tags to define dynamic content variables in the document. See [https://www.docuseal.com/examples/demo\_template.docx](https://www.docuseal.com/examples/demo_template.docx) for the specific text variable syntax, including dynamic content tables and lists. You can also use the `{{signature}}` field syntax to define fillable fields, as in a PDF.

**Related Guides:**<br>
[Use dynamic content variables in DOCX to create personalized documents](https://www.docuseal.com/guides/use-dynamic-content-variables-in-docx-to-create-personalized-documents)


```java
var submission = client.createSubmissionFromDocx(CreateSubmissionFromDocxParams.builder()
    .documents(List.of(
      CreateSubmissionFromDocxDocumentParams.builder()
        .name("string")
        .file("base64")
        .build()))
    .submitters(List.of(
      CreateSubmissionSubmitterParams.builder()
        .role("First Party")
        .email("john.doe@example.com")
        .build()))
    .name("Test Submission Document")
    .variables(Map.of("variable_name", "value"))
    .build());
```

### createSubmissionFromHtml(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#create-a-submission-from-html)

This API endpoint allows you to create a one-off submission request document using the provided HTML content, with special field tags rendered as a fillable and signable form.

**Related Guides:**<br>
[Create PDF document fillable form with HTML](https://www.docuseal.com/guides/create-pdf-document-fillable-form-with-html-api)


```java
var submission = client.createSubmissionFromHtml(CreateSubmissionFromHtmlParams.builder()
    .documents(List.of(
      CreateSubmissionFromHtmlDocumentParams.builder()
        .html("""
<p>Lorem Ipsum is simply dummy text of the
<text-field
  name="Industry"
  role="First Party"
  required="false"
  style="width: 80px; height: 16px; display: inline-block; margin-bottom: -4px">
</text-field>
and typesetting industry</p>
""")
        .name("Test Document")
        .build()))
    .submitters(List.of(
      CreateSubmissionSubmitterParams.builder()
        .role("First Party")
        .email("john.doe@example.com")
        .build()))
    .name("Test Submission Document")
    .build());
```

### updateSubmission(id, data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#update-a-submission)

Allows you to update a submission: change its name, expiration date, and archive or unarchive it.


```java
var submission = client.updateSubmission(1001, UpdateSubmissionParams.builder()
    .name("New Submission Name")
    .expireAt("2024-09-01 12:00:00 UTC")
    .archived(true)
    .build());
```

### archiveSubmission(id)

[Documentation](https://www.docuseal.com/docs/api?lang=java#archive-a-submission)

Allows you to archive a submission.


```java
client.archiveSubmission(1001);
```

### getSubmitters(params)

[Documentation](https://www.docuseal.com/docs/api?lang=java#list-all-submitters)

Provides the ability to retrieve a list of submitters.


```java
var submitters = client.getSubmitters(GetSubmittersParams.builder().limit(10).build());
```

### getSubmitter(id)

[Documentation](https://www.docuseal.com/docs/api?lang=java#get-a-submitter)

Provides functionality to retrieve information about a submitter, along with the submitter documents and field values.


```java
var submitter = client.getSubmitter(500001);
```

### updateSubmitter(id, data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#update-a-submitter)

Allows you to update submitter details, pre-fill or update field values and re-send emails.

**Related Guides:**<br>
[Automatically sign documents via API](https://www.docuseal.com/guides/pre-fill-pdf-document-form-fields-with-api#automatically_sign_documents_via_api)


```java
var submitter = client.updateSubmitter(500001, UpdateSubmitterParams.builder()
    .email("john.doe@example.com")
    .fields(List.of(
      UpdateSubmitterFieldParams.builder()
        .name("First Name")
        .value("Acme")
        .build()))
    .build());
```

### getTemplates(params)

[Documentation](https://www.docuseal.com/docs/api?lang=java#list-all-templates)

Provides the ability to retrieve a list of available document templates.


```java
var templates = client.getTemplates(GetTemplatesParams.builder().limit(10).build());
```

### getTemplate(id)

[Documentation](https://www.docuseal.com/docs/api?lang=java#get-a-template)

Provides the functionality to retrieve information about a document template.


```java
var template = client.getTemplate(1000001);
```

### createTemplateFromPdf(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#create-a-template-from-pdf)

Provides the functionality to create a fillable document template for a PDF file. Use `{{Field Name;role=Signer1;type=date}}` text tags to define fillable fields in the document. See [https://www.docuseal.com/examples/fieldtags.pdf](https://www.docuseal.com/examples/fieldtags.pdf) for more text tag formats. Or specify the exact pixel coordinates of the document fields using `fields` param.

**Related Guides:**<br>
[Use embedded text field tags to create a fillable form](https://www.docuseal.com/guides/use-embedded-text-field-tags-in-the-pdf-to-create-a-fillable-form)


```java
var template = client.createTemplateFromPdf(CreateTemplateFromPdfParams.builder()
    .documents(List.of(
      CreateTemplateFromPdfDocumentParams.builder()
        .name("string")
        .file("base64")
        .fields(List.of(
          CreateTemplateDocumentFieldParams.builder()
            .name("string")
            .areas(List.of(
              CreateTemplateDocumentFieldAreaParams.builder()
                .x(0)
                .y(0)
                .w(0)
                .h(0)
                .page(1)
                .build()))
            .build()))
        .build()))
    .name("Test PDF")
    .build());
```

### createTemplateFromDocx(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#create-a-template-from-word-docx)

Provides the functionality to create a fillable document template for an existing Microsoft Word document. Use `{{Field Name;role=Signer1;type=date}}` text tags to define fillable fields in the document. See [https://www.docuseal.com/examples/fieldtags.docx](https://www.docuseal.com/examples/fieldtags.docx) for more text tag formats. Or specify the exact pixel coordinates of the document fields using `fields` param.

**Related Guides:**<br>
[Use embedded text field tags to create a fillable form](https://www.docuseal.com/guides/use-embedded-text-field-tags-in-the-pdf-to-create-a-fillable-form)


```java
var template = client.createTemplateFromDocx(CreateTemplateFromDocxParams.builder()
    .documents(List.of(
      CreateTemplateFromDocxDocumentParams.builder()
        .name("string")
        .file("base64")
        .build()))
    .name("Test DOCX")
    .build());
```

### createTemplateFromHtml(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#create-a-template-from-html)

Provides the functionality to seamlessly generate a PDF document template by utilizing the provided HTML content while incorporating pre-defined fields.

**Related Guides:**<br>
[Create PDF document fillable form with HTML](https://www.docuseal.com/guides/create-pdf-document-fillable-form-with-html-api)


```java
var template = client.createTemplateFromHtml(CreateTemplateFromHtmlParams.builder()
    .html("""
<p>Lorem Ipsum is simply dummy text of the
<text-field
  name="Industry"
  role="First Party"
  required="false"
  style="width: 80px; height: 16px; display: inline-block; margin-bottom: -4px">
</text-field>
and typesetting industry</p>
""")
    .name("Test Template")
    .build());
```

### cloneTemplate(id, data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#clone-a-template)

Allows you to clone an existing template into a new template.


```java
var template = client.cloneTemplate(1000001, CloneTemplateParams.builder()
    .name("Cloned Template")
    .build());
```

### mergeTemplate(data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#merge-templates)

Allows you to merge multiple templates with documents and fields into a new combined template.


```java
var template = client.mergeTemplate(MergeTemplateParams.builder()
    .templateIds(List.of(321, 432))
    .name("Merged Template")
    .build());
```

### updateTemplate(id, data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#update-a-template)

Provides the functionality to move a document template to a different folder and update the name of the template.


```java
var template = client.updateTemplate(1000001, UpdateTemplateParams.builder()
    .name("New Document Name")
    .folderName("New Folder")
    .build());
```

### updateTemplateDocuments(id, data)

[Documentation](https://www.docuseal.com/docs/api?lang=java#update-template-documents)

Allows you to add, remove or replace documents in the template with provided PDF/DOCX file or HTML content.


```java
var template = client.updateTemplateDocuments(1000001, UpdateTemplateDocumentsParams.builder()
    .documents(List.of(
      UpdateTemplateDocumentsDocumentParams.builder()
        .file("string")
        .build()))
    .build());
```

### archiveTemplate(id)

[Documentation](https://www.docuseal.com/docs/api?lang=java#archive-a-template)

Allows you to archive a document template.


```java
client.archiveTemplate(1000001);
```

### Configuring Timeouts

Set timeouts (in seconds) to avoid hanging requests:

```java
var client = DocusealClient.builder()
    .apiKey(System.getenv("DOCUSEAL_API_KEY"))
    .timeout(30)
    .build();
```

## Support

For feature requests or bug reports, visit our [GitHub Issues page](https://github.com/docusealco/docuseal-java/issues).


## License

The library is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).
