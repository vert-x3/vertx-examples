
vertx.createHttpServer().requestHandler({ req ->
  println("Got form with content-type ${req.getHeader("content-type")}")
  req.setExpectMultipart(true)
  req.endHandler({ v ->
    println("firstName: ${req.getFormAttribute("firstName")}")
    println("lastName: ${req.getFormAttribute("lastName")}")
    println("male: ${req.getFormAttribute("male")}")

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
