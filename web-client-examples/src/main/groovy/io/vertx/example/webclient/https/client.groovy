import io.vertx.ext.web.client.WebClient

// Create the web client and enable SSL/TLS with a trust store
def client = WebClient.create(vertx, [
  ssl:true,
  trustStoreOptions:[
    path:"client-truststore.jks",
    password:"wibble"
  ]
])

client.get(8443, "localhost", "/").send({ ar ->
  if (ar.succeeded()) {
    def response = ar.result()
    println("Got HTTP response with status ${response.statusCode()}")
  } else {
    ar.cause().printStackTrace()
  }
})
