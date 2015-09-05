package io.vertx.example.core.verticle.worker;

import io.vertx.core.AbstractVerticle;

/**
 * An example of worker verticle
 */
public class WorkerVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    System.out.println("[Worker] Starting in " + Thread.currentThread().getName());

    vertx.eventBus().consumer("sample.data", message -> {
      System.out.println("[Worker] Consuming data in " + Thread.currentThread().getName());
      String body = (String) message.body();
      message.reply(body.toUpperCase());
    });
  }
}
