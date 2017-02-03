package io.vertx.example.circuit.breaker

import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      req.response().end("Bonjour")
    }).listen(8080)
  }
}
