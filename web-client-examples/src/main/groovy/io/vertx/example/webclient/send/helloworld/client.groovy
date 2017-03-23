import io.vertx.ext.web.client.WebClient
import io.vertx.core.buffer.Buffer

def client = WebClient.create(vertx)

def body = Buffer.buffer("Hello World")

client.put(8080, "localhost", "/").sendBuffer(body, { ar ->
  if (ar.succeeded()) {
    def response = ar.result()
    println("Got HTTP response with status ${response.statusCode()}")
  } else {
    ar.cause().printStackTrace()
  }
})
