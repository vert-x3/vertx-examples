import io.vertx.circuitbreaker.CircuitBreaker
def options = [
  maxFailures:5,
  timeout:5000,
  fallbackOnFailure:true
]

def breaker = CircuitBreaker.create("my-circuit-breaker", vertx, options).openHandler({ v ->
  println("Circuit opened")
}).closeHandler({ v ->
  println("Circuit closed")
})

def result = breaker.executeWithFallback({ future ->
  vertx.createHttpClient().getNow(8080, "localhost", "/", { response ->
    if (response.statusCode() != 200) {
      future.fail("HTTP error")
    } else {
      response.exceptionHandler(future.&fail).bodyHandler({ buffer ->
        future.complete(buffer.toString())
      })
    }
  })
}, { v ->
  // Executed when the circuit is opened
  return "Hello (fallback)"
})

result.setHandler({ ar ->
  // Do something with the result
  println("Result: ${ar.result()}")
})
