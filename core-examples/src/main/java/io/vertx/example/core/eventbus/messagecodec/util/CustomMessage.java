package io.vertx.example.core.eventbus.messagecodec.util;

/**
 * Custom message for example
 * @author Junbong
 */
public class CustomMessage {
  private final int statusCode;
  private final String resultCode;
  private final String summary;

  public CustomMessage(int statusCode, String resultCode, String summary) {
    this.statusCode = statusCode;
    this.resultCode = resultCode;
    this.summary = summary;
  }

  @Override
  public String toString() {
    return "CustomMessage{" + "statusCode=" + statusCode +
      ", resultCode='" + resultCode + '\'' +
      ", summary='" + summary + '\'' +
      '}';
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getResultCode() {
    return resultCode;
  }

  public String getSummary() {
    return summary;
  }
}
