vertx.createHttpServer().requestHandler(function (req) {
  if (req.uri() == "/") {
    // Serve the index page
    req.response().sendFile("index.html");
  } else if (req.uri().startsWith("/form")) {
    req.response().setChunked(true);
    req.setExpectMultipart(true);
    req.endHandler(function (v) {
      Array.prototype.forEach.call(req.formAttributes().names(), function(attr) {
        req.response().write("Got attr " + attr + " : " + req.formAttributes().get(attr) + "
        ");
      });
      req.response().end();
    });
  } else {
    req.response().setStatusCode(404).end();
  }
}).listen(8080);
