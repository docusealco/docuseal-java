package com.docuseal;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetSubmissionDocumentsParams {
  private Boolean merge;

  public GetSubmissionDocumentsParams merge(Boolean merge) {
    this.merge = merge;
    return this;
  }

  Map<String, Object> toQuery() {
    Map<String, Object> query = new LinkedHashMap<>();
    if (merge != null) query.put("merge", merge);
    return query;
  }
}
