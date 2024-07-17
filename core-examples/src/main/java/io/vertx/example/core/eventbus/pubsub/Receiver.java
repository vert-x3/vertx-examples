package io.vertx.example.core.eventbus.pubsub;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Receiver extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Receiver.class.getName(), "-cluster"});
  }


  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    eb.consumer("news-feed", message -> System.out.println("Received news on consumer 1: " + message.body()));

    eb.consumer("news-feed", message -> System.out.println("Received news on consumer 2: " + message.body()));

    eb.consumer("news-feed", message -> System.out.println("Received news on consumer 3: " + message.body()));

    System.out.println("Ready!");
  }
}
