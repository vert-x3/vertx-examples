package io.vertx.example.core.http.proxyconnect

import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.SelfSignedCertificate
import io.vertx.kotlin.core.http.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var certificate = SelfSignedCertificate.create()
    var serverOptions = HttpServerOptions(
      ssl = true,
      keyCertOptions = certificate.keyCertOptions())

    vertx.createHttpServer(serverOptions).requestHandler({ req ->

      println("Got request ${req.uri()}")

      for (name in req.headers().names()) {
        println("${name}: ${req.headers().get(name)}")
      }

      req.handler({ data ->
        println("Got data ${data.toString("ISO-8859-1")}")
      })

      req.endHandler({ v ->
        // Now send back a response
        req.response().setChunked(true)

        for (i in 0 until 10) {
          req.response().write("server-data-chunk-${i}")

        }


        req.response().end()
      })
    }).listen(8282)

  }
}
