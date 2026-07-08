package com.docuseal;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListSubmissionsParams {
  private Integer templateId;
  private String status;
  private String q;
  private String slug;
  private String templateFolder;
  private Boolean archived;
  private Integer limit;
  private Integer after;
  private Integer before;

  public ListSubmissionsParams templateId(Integer templateId) {
    this.templateId = templateId;
    return this;
  }

  public ListSubmissionsParams status(String status) {
    this.status = status;
    return this;
  }

  public ListSubmissionsParams q(String q) {
    this.q = q;
    return this;
  }

  public ListSubmissionsParams slug(String slug) {
    this.slug = slug;
    return this;
  }

  public ListSubmissionsParams templateFolder(String templateFolder) {
    this.templateFolder = templateFolder;
    return this;
  }

  public ListSubmissionsParams archived(Boolean archived) {
    this.archived = archived;
    return this;
  }

  public ListSubmissionsParams limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public ListSubmissionsParams after(Integer after) {
    this.after = after;
    return this;
  }

  public ListSubmissionsParams before(Integer before) {
    this.before = before;
    return this;
  }

  Map<String, Object> toQuery() {
    Map<String, Object> query = new LinkedHashMap<>();
    if (templateId != null) query.put("template_id", templateId);
    if (status != null) query.put("status", status);
    if (q != null) query.put("q", q);
    if (slug != null) query.put("slug", slug);
    if (templateFolder != null) query.put("template_folder", templateFolder);
    if (archived != null) query.put("archived", archived);
    if (limit != null) query.put("limit", limit);
    if (after != null) query.put("after", after);
    if (before != null) query.put("before", before);
    return query;
  }
}
