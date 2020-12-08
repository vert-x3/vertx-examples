package io.vertx.example.zipkin;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.tracing.HelloVerticle;
import io.vertx.tracing.zipkin.ZipkinTracingOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HelloService {

  public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setTracingOptions(new ZipkinTracingOptions().setServiceName("HelloService"))
      );

    vertx.deployVerticle(new HelloVerticle())
      .toCompletionStage()
      .toCompletableFuture()
      .get(30, TimeUnit.SECONDS);
  }
}
