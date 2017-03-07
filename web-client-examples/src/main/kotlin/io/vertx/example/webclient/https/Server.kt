package io.vertx.example.webclient.https

import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.JksOptions
import io.vertx.kotlin.core.http.*
import io.vertx.kotlin.core.net.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Start an SSL/TLS http server
    vertx.createHttpServer(HttpServerOptions(
      keyStoreOptions = JksOptions(
        path = "server-keystore.jks",
        password = "wibble"),
      ssl = true)).requestHandler({ req ->

      req.response().end()

    }).listen(8443, { listenResult ->
      if (listenResult.failed()) {
        println("Could not start HTTP server")
        listenResult.cause().printStackTrace()
      } else {
        println("Server started")
      }
    })
  }
}
