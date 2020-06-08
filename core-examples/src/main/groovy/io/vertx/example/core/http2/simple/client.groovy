import io.vertx.core.http.HttpVersion

// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

def options = [
  ssl:true,
  useAlpn:true,
  protocolVersion:"HTTP_2",
  trustAll:true
]

vertx.createHttpClient(options).get(8080, "localhost", "/").compose({ resp ->
  println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
  return resp.body()
}).onSuccess({ body ->
  println("Got data ${body.toString("ISO-8859-1")}")
}).onFailure({ err ->
  err.printStackTrace()
})
