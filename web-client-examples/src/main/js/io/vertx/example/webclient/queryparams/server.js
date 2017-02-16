
vertx.createHttpServer().requestHandler(function (req) {
  console.log("Got request with query params: " + req.query());
  req.response().end();
}).listen(8080, function (listenResult, listenResult_err) {
  if (listenResult_err != null) {
    console.log("Could not start HTTP server");
    listenResult_err.printStackTrace();
  } else {
    console.log("Server started");
  }
});
