package io.vertx.example.webclient.simple

import io.vertx.ext.web.client.WebClient

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = WebClient.create(vertx)

    client.get(8080, "localhost", "/").send({ ar ->
      if (ar.succeeded()) {
        var response = ar.result()
        println("Got HTTP response with status ${response.statusCode()} with data ${response.body().toString("ISO-8859-1")}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
