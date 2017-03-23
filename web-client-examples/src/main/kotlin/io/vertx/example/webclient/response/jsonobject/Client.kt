package io.vertx.example.webclient.response.jsonobject

import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = WebClient.create(vertx)

    client.get(8080, "localhost", "/").as(BodyCodec.jsonObject()).send({ ar ->
      if (ar.succeeded()) {
        var response = ar.result()
        println("Got HTTP response body")
        println(response.body().toString())
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
