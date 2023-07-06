package io.vertx.example.core.eventbus.pointtopoint;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.eventbus.EventBus;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Receiver extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Receiver.class.getName(), "-cluster");
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
