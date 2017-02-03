package io.vertx.example.core.http.websockets

import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().websocketHandler({ ws ->
      ws.handler({ ws.writeBinaryMessage(it) })
    }).requestHandler({ req ->
      if (req.uri() == "/") {
        req.response().sendFile("ws.html")}
    }).listen(8080)
  }
}
