
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

    (0..<10).each { i ->
      req.response().write("server-data-chunk-${i}")
    }

    req.response().end()
  })
}).listen(8282)

