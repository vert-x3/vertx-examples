package io.vertx.example.core.eventbus.pointtopoint;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Sender extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Sender.class.getName(), "-cluster"});
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();

    // Send a message every second

    vertx.setPeriodic(1000, v -> {

      eb.request("ping-address", "ping!")
        .onComplete(reply -> {
          if (reply.succeeded()) {
            System.out.println("Received reply " + reply.result().body());
          } else {
            System.out.println("No reply");
          }
        });

    });
  }
}
