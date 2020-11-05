def client = vertx.createHttpClient([:])
vertx.createHttpServer().requestHandler({ serverRequest ->
  println("Proxying request: ${serverRequest.uri()}")
  serverRequest.pause()
  def serverResponse = serverRequest.response()
  client.request(serverRequest.method(), 8282, "localhost", serverRequest.uri()).onSuccess({ clientRequest ->
    clientRequest.headers().setAll(serverRequest.headers())
    clientRequest.send(serverRequest).onSuccess({ clientResponse ->
      println("Proxying response: ${clientResponse.statusCode()}")
      serverResponse.setStatusCode(clientResponse.statusCode())
      serverResponse.headers().setAll(clientResponse.headers())
      serverResponse.send(clientResponse)
    }).onFailure({ err ->
      println("Back end failure")
      serverResponse.setStatusCode(500).end()
    })
  }).onFailure({ err ->
    println("Could not connect to localhost")
    serverResponse.setStatusCode(500).end()
  })
}).listen(8080)
