package io.vertx.example.circuit.breaker

import io.vertx.circuitbreaker.CircuitBreaker
import io.vertx.circuitbreaker.CircuitBreakerOptions
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

    breaker.executeWithFallback({ future ->
      vertx.createHttpClient().getNow(8080, "localhost", "/", { response ->
        if (response.statusCode() != 200) {
          future.fail("HTTP error")
        } else {
          response.exceptionHandler({ future.fail(it) }).bodyHandler({ buffer ->
            future.complete(buffer.toString())
          })
        }
      })
    }, { v ->
      // Executed when the circuit is opened
      return "Hello (fallback)"
    }, { ar ->
      // Do something with the result
      println("Result: ${ar.result()}")
    })
  }
}
