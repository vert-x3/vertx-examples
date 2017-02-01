import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var server = vertx.createHttpServer(io.vertx.core.http.HttpServerOptions(
    ))

    server.requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1><p>version = ${req.version()}</p></body></html>")
    }).listen(8080)
  }
}
