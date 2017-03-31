package io.vertx.example.core.http2.simple

import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpVersion
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    var options = HttpClientOptions(
      ssl = true,
      useAlpn = true,
      protocolVersion = HttpVersion.HTTP_2,
      trustAll = true)

    vertx.createHttpClient(options).getNow(8443, "localhost", "/", { resp ->
      println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
      resp.bodyHandler({ body ->
        println("Got data ${body.toString("ISO-8859-1")}")
      })
    })
  }
}
