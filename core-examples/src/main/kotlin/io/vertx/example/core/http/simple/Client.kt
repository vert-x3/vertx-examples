package io.vertx.example.core.http.simple

import io.vertx.kotlin.common.json.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpClient().getNow(8080, "localhost", "/", { resp ->
      println("Got response ${resp.statusCode()}")
      resp.bodyHandler({ body ->
        println("Got data ${body.toString("ISO-8859-1")}")
      })
    })
  }
}
