package com.docuseal;

public class DocusealException extends Exception {
  private final int statusCode;

  public DocusealException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public DocusealException(String message, Throwable cause) {
    super(message, cause);
    this.statusCode = 0;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
