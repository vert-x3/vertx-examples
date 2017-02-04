package io.vertx.example.core.http.https

import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var server = vertx.createHttpServer(io.vertx.core.http.HttpServerOptions(
      ssl = true,
      keyStoreOptions = io.vertx.core.net.JksOptions(
        path = "server-keystore.jks",
        password = "wibble")))

    server.requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>")
    }).listen(4443)
  }
}
