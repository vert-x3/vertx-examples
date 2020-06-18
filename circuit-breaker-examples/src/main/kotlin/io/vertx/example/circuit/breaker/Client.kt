package io.vertx.example.circuit.breaker

import io.vertx.circuitbreaker.CircuitBreaker
import io.vertx.circuitbreaker.CircuitBreakerOptions
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.circuitbreaker.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var options = CircuitBreakerOptions(
      maxFailures = 5,
      timeout = 5000,
      fallbackOnFailure = true)

    var breaker = CircuitBreaker.create("my-circuit-breaker", vertx, options).openHandler({ v ->
      println("Circuit opened")
    }).closeHandler({ v ->
      println("Circuit closed")
    })

    breaker.executeWithFallback({ promise ->
      vertx.createHttpClient().get(8080, "localhost", "/").compose<Any>({ resp ->
        if (resp.statusCode() != 200) {
          return Future.failedFuture<Any>("HTTP error")
        } else {
          return resp.body()
        }
      }).map({ Buffer.toString() }).onComplete(promise)
    }, { v ->
      // Executed when the circuit is opened
      return "Hello (fallback)"
    }, { ar ->
      // Do something with the result
      println("Result: ${ar.result()}")
    })
  }
}
