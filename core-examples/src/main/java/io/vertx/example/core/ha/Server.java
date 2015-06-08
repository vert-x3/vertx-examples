package io.vertx.example.core.ha;

import io.vertx.core.AbstractVerticle;

import java.lang.management.ManagementFactory;

/*
 * This is just a simple verticle creating a HTTP server. The served response contains an id identifying the process
 * for illustration purpose as it will change when the verticle is migrated.
 *
 * The verticle is intended to be launched using the `-ha` option.
 */
public class Server extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      final String name = ManagementFactory.getRuntimeMXBean().getName();
      req.response().end("Happily served by " + name);
    }).listen(8080);
  }
}
