package io.vertx.koin.example

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import org.slf4j.LoggerFactory

class GreeterVerticle(private val greeter: Greeter): AbstractVerticle() {

  override fun start(promise: Promise<Void>) {
    vertx.createHttpServer().requestHandler { request ->
      val name = request.getParam("name")
      logger.info("Got request for name: $name")
      if (name == null) request.response().setStatusCode(400).end("Missing name")
      else request.response().end(greeter.sayHello(name))
    }.listen(8080) { ar ->
      if (ar.succeeded()) {
        logger.info("GreetingVerticle started: @${this.hashCode()}")
        promise.complete()
      } else {
        promise.fail(ar.cause())
      }
    }
  }

  companion object {
      private val logger = LoggerFactory.getLogger(GreeterVerticle::class.java)
  }
}
