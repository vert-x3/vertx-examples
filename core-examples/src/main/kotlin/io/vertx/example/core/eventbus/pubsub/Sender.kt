package io.vertx.example.core.eventbus.pubsub


class Sender : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var eb = vertx.eventBus()

    // Send a message every second

    vertx.setPeriodic(1000, { v ->
      eb.publish("news-feed", "Some news!")
    })
  }
}
