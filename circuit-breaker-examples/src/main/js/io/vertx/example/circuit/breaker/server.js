vertx.createHttpServer().requestHandler(function (req) {
  req.response().end("Bonjour");
}).listen(8080);
