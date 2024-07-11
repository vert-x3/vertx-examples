package io.vertx.example.micrometer.verticles;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.Random;

/**
 * @author Joel Takvorian, jtakvori@redhat.com
 */
public final class Greetings {
  private static final String[] GREETINGS = {
          "Hello world!",
          "Bonjour monde!",
          "Hallo Welt!",
          "Hola Mundo!"
  };
  private static final Random RND = new Random();

  private Greetings() {
  }

  static Future<String> get(Vertx vertx) {
    return vertx.executeBlocking(() -> {
      // Simulate worker pool processing time between 200ms and 2s
      int processingTime = RND.nextInt(1800) + 200;
      try {
        Thread.sleep(processingTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return GREETINGS[RND.nextInt(4)];
    });
  }
}
