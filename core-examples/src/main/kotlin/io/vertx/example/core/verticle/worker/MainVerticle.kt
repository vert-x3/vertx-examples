package io.vertx.example.core.verticle.worker

import io.vertx.core.DeploymentOptions
import io.vertx.kotlin.core.*

class MainVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    println("[Main] Running in ${java.lang.Thread.currentThread().getName()}")
    vertx.deployVerticle("io.vertx.example.core.verticle.worker.WorkerVerticle", DeploymentOptions(
      worker = true))

    vertx.eventBus().request<Any>("sample.data", "hello vert.x", { r ->
      println("[Main] Receiving reply ' ${r.result().body()}' in ${java.lang.Thread.currentThread().getName()}")
    })
  }
}
