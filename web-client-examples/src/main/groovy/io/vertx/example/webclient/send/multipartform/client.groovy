import io.vertx.ext.web.client.WebClient
import io.vertx.core.MultiMap
def client = WebClient.create(vertx)

def form = MultiMap.caseInsensitiveMultiMap()
form.add("firstName", "Dale")
form.add("lastName", "Cooper")
form.add("male", "true")

client.post(8080, "localhost", "/").putHeader("content-type", "multipart/form-data").sendForm(form, { ar ->
  if (ar.succeeded()) {
    def response = ar.result()
    println("Got HTTP response with status ${response.statusCode()}")
  } else {
    ar.cause().printStackTrace()
  }
})
