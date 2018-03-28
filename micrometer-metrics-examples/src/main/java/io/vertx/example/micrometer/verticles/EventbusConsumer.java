package io.vertx.example.micrometer.verticles;

import io.vertx.core.AbstractVerticle;

/**
 * @author Joel Takvorian, jtakvori@redhat.com
 */
public class EventbusConsumer extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.eventBus().<String>consumer("greeting", message -> {
      String greeting = message.body();
      System.out.println("Received: " + greeting);
      Greetings.get(vertx, greetingResult -> message.reply(greetingResult.result()));
    });
  }
}
