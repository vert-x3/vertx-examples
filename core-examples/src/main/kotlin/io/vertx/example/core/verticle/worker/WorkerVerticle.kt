import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    println("[Worker] Starting in ${java.lang.Thread.currentThread().getName()}")

    vertx.eventBus().consumer("sample.data", { message ->
      println("[Worker] Consuming data in ${java.lang.Thread.currentThread().getName()}")
      var body = message.body()
      message.reply(body.toUpperCase())
    })
  }
}
