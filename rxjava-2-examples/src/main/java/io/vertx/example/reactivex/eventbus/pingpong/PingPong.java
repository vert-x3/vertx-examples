package io.vertx.example.reactivex.eventbus.pingpong;

import io.vertx.core.Launcher;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

public class PingPong extends AbstractVerticle {

  private static final String ADDRESS = "ping-address";

  public static void main(String[] args) {
    Launcher.executeCommand("run", PingPong.class.getName(), "-cluster");
  }

  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    eb.consumer(ADDRESS)
      .toFlowable()
      .subscribe(message -> {
        System.out.println("Received " + message.body());
        message.reply("PONG");
      });

    // Send a message every second
    vertx.setPeriodic(1000, v -> {
      eb.rxRequest(ADDRESS, "PING")
        .subscribe(reply -> {
          System.out.println("Received reply " + reply.body());
        });
        });
    }
}
