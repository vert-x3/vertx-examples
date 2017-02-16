
vertx.createHttpServer().requestHandler({ req ->

  req.response().end("Hello World")

}).listen(8080, { listenResult ->
  if (listenResult.failed()) {
    println("Could not start HTTP server")
    listenResult.cause().printStackTrace()
  } else {
    println("Server started")
  }
})
