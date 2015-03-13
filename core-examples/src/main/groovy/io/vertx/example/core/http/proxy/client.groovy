def request = vertx.createHttpClient([:]).put(8080, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")})
})

request.setChunked(true)

for (def i = 0;i < 10;i++) {
  request.write("client-chunk-${i}")
}

request.end()
