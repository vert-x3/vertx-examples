import io.vertx.core.http.HttpVersion

def options = [
  protocolVersion:"HTTP_2"
]

vertx.createHttpClient(options).getNow(8080, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})
