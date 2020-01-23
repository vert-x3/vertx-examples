package io.vertx.example.core.http.websockets


class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().webSocketHandler({ ws ->
      ws.handler({ ws.writeBinaryMessage(it) })
    }).requestHandler({ req ->
      if (req.uri() == "/") {
        req.response().sendFile("ws.html")}
    }).listen(8080)
  }
}
