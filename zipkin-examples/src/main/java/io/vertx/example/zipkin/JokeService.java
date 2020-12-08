package io.vertx.example.zipkin;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.tracing.ChuckNorrisJokesVerticle;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.tracing.zipkin.ZipkinTracingOptions;
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

    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setTracingOptions(new ZipkinTracingOptions().setServiceName("JokeService"))
      );

    vertx.deployVerticle(new ChuckNorrisJokesVerticle(options))
      .toCompletionStage()
      .toCompletableFuture()
      .get(30, TimeUnit.SECONDS);
    System.out.println("JokeService started");
  }
}
