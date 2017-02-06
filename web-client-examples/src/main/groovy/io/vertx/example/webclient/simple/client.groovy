import io.vertx.ext.web.client.WebClient

def client = WebClient.create(vertx)

client.get(8080, "localhost", "/").send({ ar ->
  if (ar.succeeded()) {
    def response = ar.result()
    println("Got HTTP response with status ${response.statusCode()} with data ${response.body().toString("ISO-8859-1")}")
  } else {
    ar.cause().printStackTrace()
  }
})
