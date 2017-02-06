import io.vertx.ext.web.client.WebClient

def client = WebClient.create(vertx)

def user = [
  firstName:"Date",
  lastName:"Cooper",
  male:true
]

client.put(8080, "localhost", "/").sendJson(user, { ar ->
  if (ar.succeeded()) {
    def response = ar.result()
    println("Got HTTP response with status ${response.statusCode()}")
  } else {
    ar.cause().printStackTrace()
  }
})
