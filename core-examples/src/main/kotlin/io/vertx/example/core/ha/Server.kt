package io.vertx.example.core.ha

import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      var name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName()
      req.response().end("Happily served by ${name}")
    }).listen(8080)
  }
}
