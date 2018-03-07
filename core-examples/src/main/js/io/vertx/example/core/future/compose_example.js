var Future = require("vertx-js/future");
var anotherAsyncAction = function(name) {
  var future = Future.future();
  // mimic something that take times
  vertx.setTimer(100, function (l) {
    future.complete("hello " + name);
  });
  return future
};
var anAsyncAction = function() {
  var future = Future.future();
  // mimic something that take times
  vertx.setTimer(100, function (l) {
    future.complete("world");
  });
  return future
};
var future = anAsyncAction();
future.compose(anotherAsyncAction).setHandler(function (ar, ar_err) {
  if (ar_err != null) {
    console.log("Something bad happened");
    ar_err.printStackTrace();
  } else {
    console.log("Result: " + ar);
  }
});
