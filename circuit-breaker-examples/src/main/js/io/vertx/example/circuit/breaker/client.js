var CircuitBreaker = require("vertx-circuit-breaker-js/circuit_breaker");
var options = {
  "maxFailures" : 5,
  "timeout" : 5000,
  "fallbackOnFailure" : true
};

var breaker = CircuitBreaker.create("my-circuit-breaker", vertx, options).openHandler(function (v) {
  console.log("Circuit opened");
}).closeHandler(function (v) {
  console.log("Circuit closed");
});

breaker.executeWithFallback(function (future) {
  vertx.createHttpClient().getNow(8080, "localhost", "/", function (response) {
    if (response.statusCode() !== 200) {
      future.fail("HTTP error");
    } else {
      response.exceptionHandler(future.fail).bodyHandler(function (buffer) {
        future.complete(buffer.toString());
      });
    }
  });
}, function (v) {
  // Executed when the circuit is opened
  return "Hello (fallback)"
}, function (ar, ar_err) {
  // Do something with the result
  console.log("Result: " + ar);
});
