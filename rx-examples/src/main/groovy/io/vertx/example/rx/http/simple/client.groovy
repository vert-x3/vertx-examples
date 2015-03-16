import io.vertx.core.http.HttpMethod
def client = vertx.createHttpClient()
def req = client.request(HttpMethod.GET, 8080, "localhost", "/")
req.toObservable().subscribe({ resp ->
  println("Got response ${resp.statusCode()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})
req.end()
