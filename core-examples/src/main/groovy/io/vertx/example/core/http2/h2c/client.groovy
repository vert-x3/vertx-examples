import io.vertx.core.http.HttpVersion

def options = [
  protocolVersion:"HTTP_2"
]

vertx.createHttpClient(options).get(8080, "localhost", "/").compose({ resp ->
  println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
  return resp.body()
}).onSuccess({ body ->
  println("Got data ${body.toString("ISO-8859-1")}")
})
