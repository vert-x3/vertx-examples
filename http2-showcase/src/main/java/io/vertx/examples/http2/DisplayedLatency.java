package io.vertx.examples.http2;

class DisplayedLatency {

  private final boolean displayed;
  private final Integer latency;

  DisplayedLatency(Integer latency, Integer queryLatency) {
    this.latency = latency;
    this.displayed = queryLatency.equals(latency);
  }

  public boolean isDisplayed() {
    return displayed;
  }

  public Integer getLatency() {
    return latency;
  }

}
