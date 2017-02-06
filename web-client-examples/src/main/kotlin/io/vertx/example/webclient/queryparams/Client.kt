package io.vertx.example.webclient.queryparams

import io.vertx.ext.web.client.WebClient

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = WebClient.create(vertx)

    client.get(8080, "localhost", "/").addQueryParam("firstName", "Dale").addQueryParam("lastName", "Cooper").addQueryParam("male", "true").send({ ar ->
      if (ar.succeeded()) {
        var response = ar.result()
        println("Got HTTP response with status ${response.statusCode()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
