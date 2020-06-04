
// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

def options = [
  ssl:true,
  trustAll:true
]
vertx.createHttpClient(options).get(4443, "localhost", "/").compose({ resp ->
  println("Got response ${resp.statusCode()}")
  return resp.body()
}).onSuccess({ body ->
  println("Got data ${body.toString("ISO-8859-1")}")
})
