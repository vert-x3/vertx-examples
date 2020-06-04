package io.vertx.example.core.http.websockets

import io.vertx.core.buffer.Buffer

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var client = vertx.createHttpClient()

    client.webSocket(8080, "localhost", "/some-uri").onSuccess({ webSocket ->
      webSocket.handler({ data ->
        println("Received data ${data.toString("ISO-8859-1")}")
        client.close()
      })
      webSocket.writeBinaryMessage(Buffer.buffer("Hello world"))
    })
  }
}
