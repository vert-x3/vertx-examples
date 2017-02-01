import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from ${this}</h1></body></html>")
    }).listen(8080)
  }
}
