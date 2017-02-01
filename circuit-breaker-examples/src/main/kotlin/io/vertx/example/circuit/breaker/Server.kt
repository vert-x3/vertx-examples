import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      req.response().end("Bonjour")
    }).listen(8080)
  }
}
