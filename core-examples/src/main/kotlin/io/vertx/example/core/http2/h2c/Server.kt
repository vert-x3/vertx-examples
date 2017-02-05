package io.vertx.example.core.http2.h2c

import io.vertx.core.http.HttpServerOptions
import io.vertx.kotlin.core.http.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var server = vertx.createHttpServer(HttpServerOptions())

    server.requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1><p>version = ${req.version()}</p></body></html>")
    }).listen(8080)
  }
}
