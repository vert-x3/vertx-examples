package io.vertx.example.core.verticle.worker


class WorkerVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    println("[Worker] Starting in ${java.lang.Thread.currentThread().getName()}")

    vertx.eventBus().consumer<String>("sample.data", { message ->
      println("[Worker] Consuming data in ${java.lang.Thread.currentThread().getName()}")
      var body = message.body()
      message.reply(body.toUpperCase())
    })
  }
}
