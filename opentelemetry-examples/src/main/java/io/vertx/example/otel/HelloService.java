package io.vertx.example.otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.resources.Resource;
import io.vertx.core.Vertx;
import io.vertx.example.tracing.HelloVerticle;
import io.vertx.tracing.opentelemetry.OpenTelemetryTracingFactory;

public class HelloService {

  public static void main(String[] args) {
    Resource resource = Resource.getDefault().toBuilder()
      .put(AttributeKey.stringKey("service.name"), "hello-service")
      .build();
    OpenTelemetry otel = OpenTelemetryConfig.configure(resource);
    Vertx vertx = Vertx.builder()
      .withTracer(new OpenTelemetryTracingFactory(otel))
      .build();
    vertx.deployVerticle(new HelloVerticle()).await();
  }
}
