package io.vertx.example.core.http2.simple

import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var server = vertx.createHttpServer(io.vertx.core.http.HttpServerOptions(
      useAlpn = true,
      ssl = true,
      pemKeyCertOptions = io.vertx.core.net.PemKeyCertOptions(
        keyPath = "server-key.pem",
        certPath = "server-cert.pem")))

    server.requestHandler({ req ->
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1><p>version = ${req.version()}</p></body></html>")
    }).listen(8443)
  }
}
