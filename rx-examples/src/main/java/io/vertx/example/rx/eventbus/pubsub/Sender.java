package io.vertx.example.rx.eventbus.pubsub;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Sender extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runClusteredExample(Sender.class);
  }

  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    // Send a message every second

    vertx.setPeriodic(1000, v -> eb.publish("news-feed", "Some news!"));
  }
}
