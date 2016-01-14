package io.vertx.example.core.eventbus.messagecodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.example.core.eventbus.messagecodec.util.CustomMessage;
import io.vertx.example.core.eventbus.messagecodec.util.CustomMessageCodec;
import io.vertx.example.util.Runner;

/**
 * Created by Bong on 2016-01-14.
 *
 * @author Bong
 * @version 1.0.0
 */
public class Sender extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runClusteredExample(Sender.class);
  }

  @Override
  public void start() throws Exception {
    EventBus eventBus = getVertx().eventBus();

    // Register codec for custom message
    eventBus.registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());

    // Custom message
    CustomMessage message = new CustomMessage(200, "A00000001", "everything ok");

    // Send message
    getVertx().setPeriodic(1000, _id -> {


      eventBus.send("custom-message-receiver", message, reply -> {
        if (reply.succeeded()) {
          System.out.println("Received reply!");
        } else {
          System.out.println("No reply");
        }
      });
    });
  }
}
