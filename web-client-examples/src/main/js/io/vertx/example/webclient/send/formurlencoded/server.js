
vertx.createHttpServer().requestHandler(function (req) {
  console.log("Got form with content-type " + req.getHeader("content-type"));
  req.setExpectMultipart(true);
  req.endHandler(function (v) {
    console.log("firstName: " + req.getFormAttribute("firstName"));
    console.log("lastName: " + req.getFormAttribute("lastName"));
    console.log("male: " + req.getFormAttribute("male"));

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
