package io.vertx.example.core.verticle.worker

import io.vertx.kotlin.common.json.*

class MainVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    println("[Main] Running in ${java.lang.Thread.currentThread().getName()}")
    vertx.deployVerticle("io.vertx.example.core.verticle.worker.WorkerVerticle", io.vertx.core.DeploymentOptions(
      worker = true))

    vertx.eventBus().send<Any>("sample.data", "hello vert.x", { r ->
      println("[Main] Receiving reply ' ${r.result().body()}' in ${java.lang.Thread.currentThread().getName()}")
    })
  }
}
