package io.vertx.example.circuit.breaker


class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      req.response().end("Bonjour")
    }).listen(8080)
  }
}
