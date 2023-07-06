package io.vertx.example.core.eventbus.messagecodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.eventbus.EventBus;
import io.vertx.example.core.eventbus.messagecodec.util.CustomMessage;
import io.vertx.example.core.eventbus.messagecodec.util.CustomMessageCodec;

/**
 * Cluster receiver
 * @author Junbong
 */
public class ClusterReceiver extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", ClusterReceiver.class.getName(), "-cluster");
  }

  @Override
  public void start() throws Exception {
    EventBus eventBus = getVertx().eventBus();

    // Register codec for custom message
    eventBus.registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());

    // Receive message
    eventBus.consumer("cluster-message-receiver", message -> {
      CustomMessage customMessage = (CustomMessage) message.body();

      System.out.println("Custom message received: "+customMessage.getSummary());

      // Replying is same as publishing
      CustomMessage replyMessage = new CustomMessage(200, "a00000002", "Message sent from cluster receiver!");
      message.reply(replyMessage);
    });
  }
}
