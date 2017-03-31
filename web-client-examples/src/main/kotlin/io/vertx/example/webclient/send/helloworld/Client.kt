package io.vertx.example.webclient.send.helloworld

import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.WebClient

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = WebClient.create(vertx)

    var body = Buffer.buffer("Hello World")

    client.put(8080, "localhost", "/").sendBuffer(body, { ar ->
      if (ar.succeeded()) {
        var response = ar.result()
        println("Got HTTP response with status ${response.statusCode()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
