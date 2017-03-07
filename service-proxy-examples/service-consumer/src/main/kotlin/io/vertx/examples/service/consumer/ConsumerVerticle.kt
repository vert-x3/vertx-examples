package io.vertx.examples.service.consumer

import io.vertx.examples.service.ProcessorService
import io.vertx.kotlin.core.json.*

class ConsumerVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var service = ProcessorService.createProxy(vertx, "vertx.processor")

    var document = json {
      obj("name" to "vertx")
    }

    service.process(document, { r ->
      if (r.succeeded()) {
        println(r.result().toString())
      } else {
        println(r.cause())
        io.vertx.examples.service.consumer.Failures.dealWithFailure(r.cause())
      }
    })
  }
}
