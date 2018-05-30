import io.vertx.core.net.ProxyType
def request = vertx.createHttpClient([
  ssl:true,
  trustAll:true,
  verifyHost:false,
  proxyOptions:[
    type:"HTTP",
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

(0..<10).each { i ->
  request.write("client-chunk-${i}")
}

request.end()
