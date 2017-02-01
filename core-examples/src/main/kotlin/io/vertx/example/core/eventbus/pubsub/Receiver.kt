import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var eb = vertx.eventBus()

    eb.consumer("news-feed", { message ->
      println("Received news on consumer 1: ${message.body()}")
    })

    eb.consumer("news-feed", { message ->
      println("Received news on consumer 2: ${message.body()}")
    })

    eb.consumer("news-feed", { message ->
      println("Received news on consumer 3: ${message.body()}")
    })

    println("Ready!")
  }
}
