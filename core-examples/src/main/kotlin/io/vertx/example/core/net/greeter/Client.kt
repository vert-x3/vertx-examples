package io.vertx.example.core.net.greeter

import io.vertx.core.parsetools.RecordParser

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createNetClient().connect(1234, "localhost", { res ->

      if (res.succeeded()) {
        var socket = res.result()

        RecordParser.newDelimited("\n", socket).endHandler({ v ->
          socket.close()
        }).exceptionHandler({ t ->
          t.printStackTrace()
          socket.close()
        }).handler({ buffer ->
          var greeting = buffer.toString("UTF-8")
          println("Net client receiving: ${greeting}")
        })

        // Now send some data
        java.util.stream.Stream.of<Any>("John", "Joe", "Lisa", "Bill").forEach({ name ->
          println("Net client sending: ${name}")
          socket.write(name).write("\n")
        })

      } else {
        println("Failed to connect ${res.cause()}")
      }
    })
  }
}
