package io.vertx.example.core.eventbus.pointtopoint


class Sender : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var eb = vertx.eventBus()

    // Send a message every second

    vertx.setPeriodic(1000, { v ->

      eb.request<Any>("ping-address", "ping!", { reply ->
        if (reply.succeeded()) {
          println("Received reply ${reply.result().body()}")
        } else {
          println("No reply")
        }
      })

    })
  }
}
