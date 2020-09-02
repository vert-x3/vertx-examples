def client = vertx.createHttpClient([:])
vertx.createHttpServer().requestHandler({ req ->
  println("Proxying request: ${req.uri()}")
  client.request(req.method(), 8282, "localhost", req.uri()).onSuccess({ c_req ->
    c_req.setChunked(true)
    c_req.headers().setAll(req.headers())
    c_req.send(req).onSuccess({ c_res ->
      println("Proxying response: ${c_res.statusCode()}")
      req.response().setChunked(true)
      req.response().setStatusCode(c_res.statusCode())
      req.response().headers().setAll(c_res.headers())
      c_res.handler({ data ->
        println("Proxying response body: ${data.toString("ISO-8859-1")}")
        req.response().write(data)
      })
      c_res.endHandler({ v ->
        req.response().end()
      })
    }).onFailure({ err ->
      println("Back end failure")
      req.response().setStatusCode(500).end()
    })
  }).onFailure({ err ->
    println("Could not connect to localhost")
    req.response().setStatusCode(500).end()
  })
}).listen(8080)
