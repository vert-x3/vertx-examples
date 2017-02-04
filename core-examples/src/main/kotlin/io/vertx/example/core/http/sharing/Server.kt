package io.vertx.example.core.http.sharing

import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.deployVerticle("io.vertx.example.core.http.sharing.HttpServerVerticle", io.vertx.core.DeploymentOptions(
      instances = 2))
  }
}
