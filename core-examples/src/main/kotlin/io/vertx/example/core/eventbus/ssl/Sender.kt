package io.vertx.example.core.eventbus.ssl


class Sender : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var eb = vertx.eventBus()

    // Send a message every second

    vertx.setPeriodic(1000, { v ->

      eb.request<Any>("ping-address", "ping!", { ar ->
        if (ar.succeeded()) {
          println("Received reply ${ar.result().body()}")
        } else {
          println("No reply")
        }
      })

    })
  }
}
