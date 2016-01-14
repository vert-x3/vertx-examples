package io.vertx.example.core.eventbus.messagecodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.example.core.eventbus.messagecodec.util.CustomMessage;
import io.vertx.example.core.eventbus.messagecodec.util.CustomMessageCodec;

/**
 * Created by Bong on 2016-01-14.
 *
 * @author Bong
 * @version 1.0.0
 */
public class LocalReceiver extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    EventBus eventBus = getVertx().eventBus();

    // Does not have to register codec because sender already registered
    /*eventBus.registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());*/

    // Receive message
    eventBus.consumer("local-message-receiver", message -> {
      CustomMessage customMessage = (CustomMessage) message.body();

      System.out.println("Custom message received: "+customMessage.getSummary());

      // Replying is same as publishing
      CustomMessage replyMessage = new CustomMessage(200, "a00000002", "Message sent from local receiver!");
      message.reply(replyMessage);
    });
  }
}
