package io.vertx.example.rxjava.eventbus.pubsub;

import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Receiver extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runClusteredExample(Receiver.class);
  }


  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    eb.consumer("news-feed").
        toObservable().
        subscribe(message -> System.out.println("Received news: " + message.body()));

    System.out.println("Ready!");
  }
}
