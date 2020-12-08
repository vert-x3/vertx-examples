package movierating

import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.coroutines.await

suspend fun main() {
  val vertx = Vertx.vertx()
  try {
    vertx.deployVerticle("movierating.App").await()
    println("Application started")
  } catch (exception: Throwable) {
    println("Could not start application")
    exception.printStackTrace()
  }
}

