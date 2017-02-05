package io.vertx.example.core.http2.customframes

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.PemKeyCertOptions
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
      var resp = req.response()

      req.customFrameHandler({ frame ->
        println("Received client frame ${frame.payload().toString("UTF-8")}")

        // Write the sam
        resp.writeCustomFrame(10, 0, Buffer.buffer("pong"))
      })
    }).listen(8443)
  }
}
