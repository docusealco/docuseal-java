package com.docuseal;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListSubmittersParams {
  private Integer submissionId;
  private String q;
  private String slug;
  private OffsetDateTime completedAfter;
  private OffsetDateTime completedBefore;
  private String externalId;
  private Integer limit;
  private Integer after;
  private Integer before;

  public ListSubmittersParams submissionId(Integer submissionId) {
    this.submissionId = submissionId;
    return this;
  }

  public ListSubmittersParams q(String q) {
    this.q = q;
    return this;
  }

  public ListSubmittersParams slug(String slug) {
    this.slug = slug;
    return this;
  }

  public ListSubmittersParams completedAfter(OffsetDateTime completedAfter) {
    this.completedAfter = completedAfter;
    return this;
  }

  public ListSubmittersParams completedBefore(OffsetDateTime completedBefore) {
    this.completedBefore = completedBefore;
    return this;
  }

  public ListSubmittersParams externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  public ListSubmittersParams limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public ListSubmittersParams after(Integer after) {
    this.after = after;
    return this;
  }

  public ListSubmittersParams before(Integer before) {
    this.before = before;
    return this;
  }

  Map<String, Object> toQuery() {
    Map<String, Object> query = new LinkedHashMap<>();
    if (submissionId != null) query.put("submission_id", submissionId);
    if (q != null) query.put("q", q);
    if (slug != null) query.put("slug", slug);
    if (completedAfter != null) query.put("completed_after", completedAfter);
    if (completedBefore != null) query.put("completed_before", completedBefore);
    if (externalId != null) query.put("external_id", externalId);
    if (limit != null) query.put("limit", limit);
    if (after != null) query.put("after", after);
    if (before != null) query.put("before", before);
    return query;
  }
}
