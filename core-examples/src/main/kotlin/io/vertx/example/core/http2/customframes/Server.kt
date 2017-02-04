package io.vertx.example.core.http2.customframes

import io.vertx.core.buffer.Buffer
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
      var resp = req.response()

      req.customFrameHandler({ frame ->
        println("Received client frame ${frame.payload().toString("UTF-8")}")

        // Write the sam
        resp.writeCustomFrame(10, 0, Buffer.buffer("pong"))
      })
    }).listen(8443)
  }
}
