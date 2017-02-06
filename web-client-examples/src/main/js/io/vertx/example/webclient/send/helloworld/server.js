
vertx.createHttpServer().requestHandler(function (req) {

  req.bodyHandler(function (buff) {
    console.log("Receiving user " + buff + " from client ");
    req.response().end();
  });

}).listen(8080, function (listenResult, listenResult_err) {
  if (listenResult_err != null) {
    console.log("Could not start HTTP server");
    listenResult_err.printStackTrace();
  } else {
    console.log("Server started");
  }
});
