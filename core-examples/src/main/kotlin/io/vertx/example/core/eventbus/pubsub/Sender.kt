import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var eb = vertx.eventBus()

    // Send a message every second

    vertx.setPeriodic(1000, { v ->
      eb.publish("news-feed", "Some news!")
    })
  }
}
