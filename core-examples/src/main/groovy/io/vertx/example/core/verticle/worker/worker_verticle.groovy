println("[Worker] Starting in ${java.lang.Thread.currentThread().getName()}")

vertx.eventBus().consumer("sample.data", { message ->
  println("[Worker] Consuming data in ${java.lang.Thread.currentThread().getName()}")
  def body = message.body()
  message.reply(body.toUpperCase())
})
