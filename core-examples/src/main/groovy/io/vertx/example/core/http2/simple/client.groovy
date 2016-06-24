import io.vertx.core.http.HttpVersion

// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

def options = [
  ssl:true,
  useAlpn:true,
  protocolVersion:"HTTP_2",
  trustAll:true
]

vertx.createHttpClient(options).getNow(8443, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})
