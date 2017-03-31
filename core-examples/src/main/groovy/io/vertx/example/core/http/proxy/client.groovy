def request = vertx.createHttpClient([:]).put(8080, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})

request.setChunked(true)

(0..<10).each { i ->
  request.write("client-chunk-${i}")
}

request.end()
