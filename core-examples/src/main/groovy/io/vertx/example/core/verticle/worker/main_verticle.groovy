println("[Main] Running in ${java.lang.Thread.currentThread().getName()}")
vertx.deployVerticle("io.vertx.example.core.verticle.worker.WorkerVerticle", [
  worker:true
])

vertx.eventBus().request("sample.data", "hello vert.x", { r ->
  println("[Main] Receiving reply ' ${r.result().body()}' in ${java.lang.Thread.currentThread().getName()}")
})
