package io.vertx.example.core.net.echossl

import io.vertx.core.net.NetClientOptions
import io.vertx.kotlin.core.net.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = NetClientOptions(
      ssl = true,
      trustAll = true)

    vertx.createNetClient(options).connect(1234, "localhost", { res ->
      if (res.succeeded()) {
        var sock = res.result()
        sock.handler({ buff ->
          println("client receiving ${buff.toString("UTF-8")}")
        })

        // Now send some data
        for (i in 0 until 10) {
          var str = "hello ${i}\n"
          println("Net client sending: ${str}")
          sock.write(str)

        }

      } else {
        println("Failed to connect ${res.cause()}")
      }
    })
  }
}
