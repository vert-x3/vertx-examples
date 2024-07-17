package io.vertx.example.core.eventbus.ssl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.launcher.application.HookContext;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.launcher.application.VertxApplicationHooks;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Receiver extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication application = new VertxApplication(new String[]{Receiver.class.getName(), "-cluster"}, new VertxApplicationHooks() {
      @Override
      public void beforeStartingVertx(HookContext context) {
        context.vertxOptions().setEventBusOptions(new EventBusOptions()
          .setSsl(true)
          .setKeyCertOptions(new JksOptions().setPath("io/vertx/example/core/eventbus/ssl/keystore.jks").setPassword("wibble"))
          .setTrustOptions(new JksOptions().setPath("io/vertx/example/core/eventbus/ssl/keystore.jks").setPassword("wibble")));
      }
    });
    application.launch();
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
