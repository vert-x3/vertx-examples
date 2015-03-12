package io.vertx.example.core.eventbus.pointtopoint;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.example.util.Runner;

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

    eb.consumer("ping-address", message -> {

      System.out.println("Received message: " + message.body());
      // Now send back reply
      message.reply("pong!");
    });

    System.out.println("Receiver ready!");
  }
}
