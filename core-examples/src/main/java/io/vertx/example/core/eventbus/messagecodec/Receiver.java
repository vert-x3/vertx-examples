package io.vertx.example.core.eventbus.messagecodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.core.eventbus.messagecodec.util.CustomMessage;
import io.vertx.example.util.Runner;

/**
 * Created by Bong on 2016-01-14.
 *
 * @author Bong
 * @version 1.0.0
 */
public class Receiver extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runClusteredExample(Receiver.class);
  }

  @Override
  public void start() throws Exception {
    getVertx().eventBus().consumer("custom-message-receiver", message -> {
      CustomMessage customMessage = (CustomMessage) message.body();

      System.out.println("Custom message received: "+customMessage);

      message.reply(null);
    });
  }
}
