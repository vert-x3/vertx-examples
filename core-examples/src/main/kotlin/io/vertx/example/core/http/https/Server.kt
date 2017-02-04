package io.vertx.example.core.http.https

import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.JksOptions
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.core.http.*
import io.vertx.kotlin.core.net.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var server = vertx.createHttpServer(HttpServerOptions(
      ssl = true,
      keyStoreOptions = JksOptions(
        path = "server-keystore.jks",
        password = "wibble")))

    server.requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>")
    }).listen(4443)
  }
}
