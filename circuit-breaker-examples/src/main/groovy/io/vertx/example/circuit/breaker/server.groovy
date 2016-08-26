vertx.createHttpServer().requestHandler({ req ->
  req.response().end("Bonjour")
}).listen(8080)
