vertx.createHttpClient().getNow(8080, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})
