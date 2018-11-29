package movierating

import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait

suspend fun main() {
  val vertx = Vertx.vertx()
  try {
    vertx.deployVerticleAwait("movierating.App")
    println("Application started")
  } catch (exception: Throwable) {
    println("Could not start application")
    exception.printStackTrace()
  }
}

