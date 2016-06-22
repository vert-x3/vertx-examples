def request = vertx.createHttpClient([
  proxyOptions:[
    host:"localhost",
    port:8080
  ]
]).put(8282, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})

request.setChunked(true)

for (def i = 0;i < 10;i++) {
  request.write("client-chunk-${i}")
}

request.end()
