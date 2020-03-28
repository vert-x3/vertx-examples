var Promise = require("vertx-js/promise");
var anotherAsyncAction = function(name) {
  var promise = Promise.promise();
  // mimic something that take times
  vertx.setTimer(100, function (l) {
    promise.complete("hello " + name);
  });
  return promise.future()
};
var anAsyncAction = function() {
  var promise = Promise.promise();
  // mimic something that take times
  vertx.setTimer(100, function (l) {
    promise.complete("world");
  });
  return promise.future()
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
