package io.vertx.example.core.eventbus.ssl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Sender extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runClusteredExample(Sender.class,
        new VertxOptions().setEventBusOptions(new EventBusOptions()
            .setSsl(true)
            .setKeyStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
            .setTrustStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
        )
    );
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();

    // Send a message every second

    vertx.setPeriodic(1000, v -> {

      eb.request("ping-address", "ping!", ar -> {
        if (ar.succeeded()) {
          System.out.println("Received reply " + ar.result().body());
        } else {
          System.out.println("No reply");
        }
      });

    });
  }
}
