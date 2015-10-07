package io.vertx.example.core.ha;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.util.ExampleRunner;
import io.vertx.example.util.Runner;

import java.lang.management.ManagementFactory;

/*
 * This is just a simple verticle creating a HTTP server. The served response contains an id identifying the process
 * for illustration purpose as it will change when the verticle is migrated.
 *
 * The verticle is intended to be launched using the `-ha` option.
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Launcher.main(new String[] { "run", Server.class.getName(), "-ha"});
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      final String name = ManagementFactory.getRuntimeMXBean().getName();
      req.response().end("Happily served by " + name);
    }).listen(8080);
  }
}
