import io.vertx.ext.consul.ConsulClient
def consulClient = ConsulClient.create(vertx)
consulClient.putValue("key11", "value11").compose({ v ->
  println("KV pair saved")
  return consulClient.getValue("key11")
}).onComplete({ ar ->
  if (ar.succeeded()) {
    println("KV pair retrieved")
    println(ar.result().value)
  } else {
    ar.cause().printStackTrace()
  }
})
