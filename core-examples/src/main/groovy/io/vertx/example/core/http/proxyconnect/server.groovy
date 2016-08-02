
vertx.createHttpServer().requestHandler({ req ->

  println("Got request ${req.uri()}")

  req.headers().names().each { name ->
    println("${name}: ${req.headers().get(name)}")
  }

  req.handler({ data ->
    println("Got data ${data.toString("ISO-8859-1")}")
  })

  req.endHandler({ v ->
    // Now send back a response
    req.response().setChunked(true)

    for (def i = 0;i < 10;i++) {
      req.response().write("server-data-chunk-${i}")
    }

    req.response().end()
  })
}).listen(8282)

