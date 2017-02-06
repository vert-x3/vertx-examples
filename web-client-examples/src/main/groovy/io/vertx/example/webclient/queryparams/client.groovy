import io.vertx.ext.web.client.WebClient

def client = WebClient.create(vertx)

client.get(8080, "localhost", "/").addQueryParam("firstName", "Dale").addQueryParam("lastName", "Cooper").addQueryParam("male", "true").send({ ar ->
  if (ar.succeeded()) {
    def response = ar.result()
    println("Got HTTP response with status ${response.statusCode()}")
  } else {
    ar.cause().printStackTrace()
  }
})
