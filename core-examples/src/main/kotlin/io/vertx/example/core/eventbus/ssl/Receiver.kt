package io.vertx.example.core.eventbus.ssl

import io.vertx.kotlin.common.json.*

class Receiver : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var eb = vertx.eventBus()

    eb.consumer<Any>("ping-address", { message ->

      println("Received message: ${message.body()}")
      // Now send back reply
      message.reply("pong!")
    })

    println("Receiver ready!")
  }
}
