package io.vertx.example.core.http.sharing

import io.vertx.core.DeploymentOptions
import io.vertx.kotlin.core.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.deployVerticle("io.vertx.example.core.http.sharing.HttpServerVerticle", DeploymentOptions(
      instances = 2))
  }
}
