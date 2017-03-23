import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec

def client = WebClient.create(vertx)

client.get(8080, "localhost", "/").as(BodyCodec.jsonObject()).send({ ar ->
  if (ar.succeeded()) {
    def response = ar.result()
    println("Got HTTP response body")
    println(groovy.json.JsonOutput.toJson(response.body()))
  } else {
    ar.cause().printStackTrace()
  }
})
