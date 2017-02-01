import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().websocketHandler({ ws ->
      ws.handler({ ws.writeBinaryMessage(it) })
    }).requestHandler({ req ->
      if (req.uri() == "/") {
        req.response().sendFile("ws.html")}
    }).listen(8080)
  }
}
