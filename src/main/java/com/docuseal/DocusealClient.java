package com.docuseal;

import com.docuseal.models.AddDocumentToTemplateRequest;
import com.docuseal.models.ArchiveSubmissionResponse;
import com.docuseal.models.ArchiveTemplateResponse;
import com.docuseal.models.CloneTemplateRequest;
import com.docuseal.models.CreateSubmissionFromDocxRequest;
import com.docuseal.models.CreateSubmissionFromHtmlRequest;
import com.docuseal.models.CreateSubmissionFromPdfRequest;
import com.docuseal.models.CreateSubmissionFromPdfResponse;
import com.docuseal.models.CreateSubmissionRequest;
import com.docuseal.models.CreateSubmissionsFromEmailsRequest;
import com.docuseal.models.CreateSubmissionsFromEmailsResponseInner;
import com.docuseal.models.CreateTemplateFromDocxRequest;
import com.docuseal.models.CreateTemplateFromHtmlRequest;
import com.docuseal.models.CreateTemplateFromPdfRequest;
import com.docuseal.models.GetSubmissionDocumentsResponse;
import com.docuseal.models.GetSubmissionResponse;
import com.docuseal.models.GetSubmissionsResponse;
import com.docuseal.models.GetSubmitterResponse;
import com.docuseal.models.GetSubmittersResponse;
import com.docuseal.models.GetTemplateResponse;
import com.docuseal.models.GetTemplatesResponse;
import com.docuseal.models.MergeTemplateRequest;
import com.docuseal.models.UpdateSubmitterRequest;
import com.docuseal.models.UpdateSubmitterResponse;
import com.docuseal.models.UpdateTemplateRequest;
import com.docuseal.models.UpdateTemplateResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DocusealClient {
  public static final String GLOBAL_URL = "https://api.docuseal.com";
  public static final String EU_URL = "https://api.docuseal.eu";

  private static final String VERSION = "1.0.0";

  private final String key;
  private final String baseUrl;
  private final HttpClient http;
  private final ObjectMapper mapper;

  public DocusealClient(String key) {
    this(key, GLOBAL_URL);
  }

  public DocusealClient(String key, String baseUrl) {
    this(key, baseUrl, HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).build());
  }

  public DocusealClient(String key, String baseUrl, HttpClient http) {
    this.key = key;
    this.baseUrl = baseUrl;
    this.http = http;
    this.mapper = JSON.getDefault().getMapper();
  }

  public GetTemplatesResponse listTemplates() throws DocusealException {
    return listTemplates(new ListTemplatesParams());
  }

  public GetTemplatesResponse listTemplates(ListTemplatesParams params) throws DocusealException {
    return get("/templates", params.toQuery(), GetTemplatesResponse.class);
  }

  public GetTemplateResponse getTemplate(int id) throws DocusealException {
    return get("/templates/" + id, Map.of(), GetTemplateResponse.class);
  }

  public GetTemplateResponse createTemplateFromDocx(CreateTemplateFromDocxRequest data) throws DocusealException {
    return send("POST", "/templates/docx", Map.of(), data, GetTemplateResponse.class);
  }

  public GetTemplateResponse createTemplateFromHtml(CreateTemplateFromHtmlRequest data) throws DocusealException {
    return send("POST", "/templates/html", Map.of(), data, GetTemplateResponse.class);
  }

  public GetTemplateResponse createTemplateFromPdf(CreateTemplateFromPdfRequest data) throws DocusealException {
    return send("POST", "/templates/pdf", Map.of(), data, GetTemplateResponse.class);
  }

  public GetTemplateResponse mergeTemplates(MergeTemplateRequest data) throws DocusealException {
    return send("POST", "/templates/merge", Map.of(), data, GetTemplateResponse.class);
  }

  public GetTemplateResponse cloneTemplate(int id, CloneTemplateRequest data) throws DocusealException {
    return send("POST", "/templates/" + id + "/clone", Map.of(), data, GetTemplateResponse.class);
  }

  public UpdateTemplateResponse updateTemplate(int id, UpdateTemplateRequest data) throws DocusealException {
    return send("PUT", "/templates/" + id, Map.of(), data, UpdateTemplateResponse.class);
  }

  public GetTemplateResponse updateTemplateDocuments(int id, AddDocumentToTemplateRequest data) throws DocusealException {
    return send("PUT", "/templates/" + id + "/documents", Map.of(), data, GetTemplateResponse.class);
  }

  public ArchiveTemplateResponse archiveTemplate(int id) throws DocusealException {
    return send("DELETE", "/templates/" + id, Map.of(), null, ArchiveTemplateResponse.class);
  }

  public ArchiveTemplateResponse permanentlyDeleteTemplate(int id) throws DocusealException {
    return send("DELETE", "/templates/" + id, Map.of("permanently", true), null, ArchiveTemplateResponse.class);
  }

  public GetSubmissionsResponse listSubmissions() throws DocusealException {
    return listSubmissions(new ListSubmissionsParams());
  }

  public GetSubmissionsResponse listSubmissions(ListSubmissionsParams params) throws DocusealException {
    return get("/submissions", params.toQuery(), GetSubmissionsResponse.class);
  }

  public GetSubmissionResponse getSubmission(int id) throws DocusealException {
    return get("/submissions/" + id, Map.of(), GetSubmissionResponse.class);
  }

  public GetSubmissionDocumentsResponse getSubmissionDocuments(int id) throws DocusealException {
    return getSubmissionDocuments(id, new GetSubmissionDocumentsParams());
  }

  public GetSubmissionDocumentsResponse getSubmissionDocuments(int id, GetSubmissionDocumentsParams params) throws DocusealException {
    return get("/submissions/" + id + "/documents", params.toQuery(), GetSubmissionDocumentsResponse.class);
  }

  public CreateSubmissionResponse createSubmission(CreateSubmissionRequest data) throws DocusealException {
    return send("POST", "/submissions/init", Map.of(), data, CreateSubmissionResponse.class);
  }

  public List<CreateSubmissionsFromEmailsResponseInner> createSubmissionFromEmails(CreateSubmissionsFromEmailsRequest data) throws DocusealException {
    return send("POST", "/submissions/emails", Map.of(), data, new TypeReference<List<CreateSubmissionsFromEmailsResponseInner>>() {});
  }

  public CreateSubmissionFromPdfResponse createSubmissionFromPdf(CreateSubmissionFromPdfRequest data) throws DocusealException {
    return send("POST", "/submissions/pdf", Map.of(), data, CreateSubmissionFromPdfResponse.class);
  }

  public CreateSubmissionFromPdfResponse createSubmissionFromDocx(CreateSubmissionFromDocxRequest data) throws DocusealException {
    return send("POST", "/submissions/docx", Map.of(), data, CreateSubmissionFromPdfResponse.class);
  }

  public CreateSubmissionFromPdfResponse createSubmissionFromHtml(CreateSubmissionFromHtmlRequest data) throws DocusealException {
    return send("POST", "/submissions/html", Map.of(), data, CreateSubmissionFromPdfResponse.class);
  }

  public ArchiveSubmissionResponse archiveSubmission(int id) throws DocusealException {
    return send("DELETE", "/submissions/" + id, Map.of(), null, ArchiveSubmissionResponse.class);
  }

  public ArchiveSubmissionResponse permanentlyDeleteSubmission(int id) throws DocusealException {
    return send("DELETE", "/submissions/" + id, Map.of("permanently", true), null, ArchiveSubmissionResponse.class);
  }

  public GetSubmittersResponse listSubmitters() throws DocusealException {
    return listSubmitters(new ListSubmittersParams());
  }

  public GetSubmittersResponse listSubmitters(ListSubmittersParams params) throws DocusealException {
    return get("/submitters", params.toQuery(), GetSubmittersResponse.class);
  }

  public GetSubmitterResponse getSubmitter(int id) throws DocusealException {
    return get("/submitters/" + id, Map.of(), GetSubmitterResponse.class);
  }

  public UpdateSubmitterResponse updateSubmitter(int id, UpdateSubmitterRequest data) throws DocusealException {
    return send("PUT", "/submitters/" + id, Map.of(), data, UpdateSubmitterResponse.class);
  }

  private <T> T get(String path, Map<String, Object> query, Class<T> type) throws DocusealException {
    return send("GET", path, query, null, type);
  }

  private <T> T send(String method, String path, Map<String, Object> query, Object body, Class<T> type) throws DocusealException {
    String data = request(method, path, query, body);
    try {
      return mapper.readValue(data, type);
    } catch (IOException e) {
      throw new DocusealException("docuseal: failed to parse response", e);
    }
  }

  private <T> T send(String method, String path, Map<String, Object> query, Object body, TypeReference<T> type) throws DocusealException {
    String data = request(method, path, query, body);
    try {
      return mapper.readValue(data, type);
    } catch (IOException e) {
      throw new DocusealException("docuseal: failed to parse response", e);
    }
  }

  private String request(String method, String path, Map<String, Object> query, Object body) throws DocusealException {
    try {
      HttpRequest.BodyPublisher payload = HttpRequest.BodyPublishers.noBody();
      if (body != null) {
        payload = HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body));
      }

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path + queryString(query)))
          .header("X-Auth-Token", key)
          .header("User-Agent", "docuseal-java/" + VERSION)
          .header("Accept", "application/json")
          .header("Content-Type", "application/json")
          .method(method, payload)
          .build();

      HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 400) {
        String message = "unexpected status " + response.statusCode();
        try {
          Map<String, Object> error = mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
          if (error.get("error") != null) {
            message = error.get("error").toString();
          }
        } catch (IOException ignored) {
          // keep the generic message when the error body is not JSON
        }
        throw new DocusealException(response.statusCode(), message);
      }

      return response.body();
    } catch (IOException e) {
      throw new DocusealException("docuseal: request failed", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DocusealException("docuseal: request interrupted", e);
    }
  }

  private String queryString(Map<String, Object> query) {
    if (query == null || query.isEmpty()) {
      return "";
    }

    StringJoiner joiner = new StringJoiner("&", "?", "");
    query.forEach((name, value) -> joiner.add(
        URLEncoder.encode(name, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8)));

    return joiner.toString();
  }
}
