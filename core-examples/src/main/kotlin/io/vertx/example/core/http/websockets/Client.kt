package io.vertx.example.core.http.websockets

import io.vertx.core.buffer.Buffer

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var client = vertx.createHttpClient()

    client.websocket(8080, "localhost", "/some-uri", { websocket ->
      websocket.handler({ data ->
        println("Received data ${data.toString("ISO-8859-1")}")
        client.close()
      })
      websocket.writeBinaryMessage(Buffer.buffer("Hello world"))
    })
  }
}
