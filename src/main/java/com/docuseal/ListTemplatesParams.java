package com.docuseal;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListTemplatesParams {
  private String q;
  private String slug;
  private String externalId;
  private String folder;
  private Boolean archived;
  private Boolean shared;
  private Integer limit;
  private Integer after;
  private Integer before;

  public ListTemplatesParams q(String q) {
    this.q = q;
    return this;
  }

  public ListTemplatesParams slug(String slug) {
    this.slug = slug;
    return this;
  }

  public ListTemplatesParams externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  public ListTemplatesParams folder(String folder) {
    this.folder = folder;
    return this;
  }

  public ListTemplatesParams archived(Boolean archived) {
    this.archived = archived;
    return this;
  }

  public ListTemplatesParams shared(Boolean shared) {
    this.shared = shared;
    return this;
  }

  public ListTemplatesParams limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public ListTemplatesParams after(Integer after) {
    this.after = after;
    return this;
  }

  public ListTemplatesParams before(Integer before) {
    this.before = before;
    return this;
  }

  Map<String, Object> toQuery() {
    Map<String, Object> query = new LinkedHashMap<>();
    if (q != null) query.put("q", q);
    if (slug != null) query.put("slug", slug);
    if (externalId != null) query.put("external_id", externalId);
    if (folder != null) query.put("folder", folder);
    if (archived != null) query.put("archived", archived);
    if (shared != null) query.put("shared", shared);
    if (limit != null) query.put("limit", limit);
    if (after != null) query.put("after", after);
    if (before != null) query.put("before", before);
    return query;
  }
}
