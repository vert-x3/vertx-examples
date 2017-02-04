package io.vertx.example.core.http2.simple

import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.PemKeyCertOptions
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.core.http.*
import io.vertx.kotlin.core.net.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var server = vertx.createHttpServer(HttpServerOptions(
      useAlpn = true,
      ssl = true,
      pemKeyCertOptions = PemKeyCertOptions(
        keyPath = "server-key.pem",
        certPath = "server-cert.pem")))

    server.requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1><p>version = ${req.version()}</p></body></html>")
    }).listen(8443)
  }
}
