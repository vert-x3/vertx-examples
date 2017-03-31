package io.vertx.example.core.http.sharing


class HttpServerVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from ${this}</h1></body></html>")
    }).listen(8080)
  }
}
