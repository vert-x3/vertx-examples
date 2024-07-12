package io.vertx.example.opentracing;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.tracing.ChuckNorrisJokesVerticle;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.tracing.opentracing.OpenTracingOptions;
import io.vertx.tracing.opentracing.OpenTracingTracerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JokeService {

  public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>();
    postgres.start();
    PgConnectOptions options = new PgConnectOptions()
      .setPort(postgres.getMappedPort(5432))
      .setHost(postgres.getContainerIpAddress())
      .setDatabase(postgres.getDatabaseName())
      .setUser(postgres.getUsername())
      .setPassword(postgres.getPassword());

    Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
      .withType(ConstSampler.TYPE)
      .withParam(1);

    Configuration.ReporterConfiguration reporterConfig = new Configuration.ReporterConfiguration()
      .withLogSpans(true);

    Configuration config = new Configuration("JokeService")
      .withSampler(samplerConfig)
      .withReporter(reporterConfig);

    Vertx vertx = Vertx.builder()
      .withTracer(new OpenTracingTracerFactory(config.getTracer()))
      .build();

    vertx.deployVerticle(new ChuckNorrisJokesVerticle(options))
      .toCompletionStage()
      .toCompletableFuture()
      .get(30, TimeUnit.SECONDS);
    System.out.println("JokeService started");
  }
}
