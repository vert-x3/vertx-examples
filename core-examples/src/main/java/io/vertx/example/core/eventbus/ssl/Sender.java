package io.vertx.example.core.eventbus.ssl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.net.JksOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Sender extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher launcher = new Launcher() {
      @Override
      public void beforeStartingVertx(VertxOptions options) {
        options.setEventBusOptions(new EventBusOptions()
          .setSsl(true)
          .setKeyCertOptions(new JksOptions().setPath("io/vertx/example/core/eventbus/ssl/keystore.jks").setPassword("wibble"))
          .setTrustOptions(new JksOptions().setPath("io/vertx/example/core/eventbus/ssl/keystore.jks").setPassword("wibble")));
      }
    };
    launcher.dispatch(new String[]{"run", Sender.class.getName(), "-cluster"});
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();

    // Send a message every second

    vertx.setPeriodic(1000, v -> {

      eb.request("ping-address", "ping!").onComplete(ar -> {
        if (ar.succeeded()) {
          System.out.println("Received reply " + ar.result().body());
        } else {
          System.out.println("No reply");
        }
      });

    });
  }
}
