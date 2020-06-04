vertx.createHttpClient().get(8080, "localhost", "/").compose({ resp ->
  println("Got response ${resp.statusCode()}")
  return resp.body()
}).onSuccess({ body ->
  println("Got data ${body.toString("ISO-8859-1")}")
})
