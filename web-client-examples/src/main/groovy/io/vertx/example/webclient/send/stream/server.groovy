
vertx.createHttpServer().requestHandler({ req ->

  req.bodyHandler({ buff ->
    println("Receiving user ${buff} from client ")
    req.response().end()
  })

}).listen(8080, { listenResult ->
  if (listenResult.failed()) {
    println("Could not start HTTP server")
    listenResult.cause().printStackTrace()
  } else {
    println("Server started")
  }
})
