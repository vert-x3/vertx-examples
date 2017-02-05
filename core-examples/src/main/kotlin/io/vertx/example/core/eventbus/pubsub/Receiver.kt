package io.vertx.example.core.eventbus.pubsub


class Receiver : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var eb = vertx.eventBus()

    eb.consumer<Any>("news-feed", { message ->
      println("Received news on consumer 1: ${message.body()}")
    })

    eb.consumer<Any>("news-feed", { message ->
      println("Received news on consumer 2: ${message.body()}")
    })

    eb.consumer<Any>("news-feed", { message ->
      println("Received news on consumer 3: ${message.body()}")
    })

    println("Ready!")
  }
}
