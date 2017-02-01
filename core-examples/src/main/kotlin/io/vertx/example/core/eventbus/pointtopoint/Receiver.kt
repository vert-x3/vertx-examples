import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var eb = vertx.eventBus()

    eb.consumer("ping-address", { message ->

      println("Received message: ${message.body()}")
      // Now send back reply
      message.reply("pong!")
    })

    println("Receiver ready!")
  }
}
