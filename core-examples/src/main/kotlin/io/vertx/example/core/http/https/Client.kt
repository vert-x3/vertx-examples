package io.vertx.example.core.http.https

import io.vertx.kotlin.common.json.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    vertx.createHttpClient(io.vertx.core.http.HttpClientOptions(
      ssl = true,
      trustAll = true)).getNow(4443, "localhost", "/", { resp ->
      println("Got response ${resp.statusCode()}")
      resp.bodyHandler({ body ->
        println("Got data ${body.toString("ISO-8859-1")}")
      })
    })
  }
}
