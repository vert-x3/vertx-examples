import io.vertx.circuitbreaker.CircuitBreaker
import io.vertx.core.buffer.Buffer
import io.vertx.core.Future
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

breaker.executeWithFallback({ promise ->
  vertx.createHttpClient().get(8080, "localhost", "/").compose({ resp ->
    if (resp.statusCode() != 200) {
      return Future.failedFuture("HTTP error")
    } else {
      return resp.body()
    }
  }).map(Buffer.&toString).onComplete(promise)
}, { v ->
  // Executed when the circuit is opened
  return "Hello (fallback)"
}, { ar ->
  // Do something with the result
  println("Result: ${ar.result()}")
})
