package com.docuseal;

import com.docuseal.models.CreateSubmissionsFromEmailsResponseInner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// POST /submissions/init is not in the public OpenAPI spec yet,
// so its response type is defined by hand.
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSubmissionResponse {
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("submitters")
  private List<CreateSubmissionsFromEmailsResponseInner> submitters;

  @JsonProperty("expired_at")
  private String expiredAt;

  @JsonProperty("created_at")
  private String createdAt;

  public Integer getId() {
    return id;
  }

  public List<CreateSubmissionsFromEmailsResponseInner> getSubmitters() {
    return submitters;
  }

  public String getExpiredAt() {
    return expiredAt;
  }

  public String getCreatedAt() {
    return createdAt;
  }
}
