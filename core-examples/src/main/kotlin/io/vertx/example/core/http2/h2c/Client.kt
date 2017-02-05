package io.vertx.example.core.http2.h2c

import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpVersion
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = HttpClientOptions(
      protocolVersion = HttpVersion.HTTP_2)

    vertx.createHttpClient(options).getNow(8080, "localhost", "/", { resp ->
      println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
      resp.bodyHandler({ body ->
        println("Got data ${body.toString("ISO-8859-1")}")
      })
    })
  }
}
