package io.vertx.example.opentracing;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.tracing.GatewayVerticle;
import io.vertx.tracing.opentracing.OpenTracingOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Gateway {

  public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

    Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
      .withType(ConstSampler.TYPE)
      .withParam(1);

    Configuration.ReporterConfiguration reporterConfig = new Configuration.ReporterConfiguration()
      .withLogSpans(true);

    Configuration config = new Configuration("Gateway")
      .withSampler(samplerConfig)
      .withReporter(reporterConfig);

    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setTracingOptions(new OpenTracingOptions(config.getTracer())
      ));

    vertx.deployVerticle(new GatewayVerticle())
      .toCompletionStage()
      .toCompletableFuture()
      .get(30, TimeUnit.SECONDS);
  }
}
