import io.vertx.core.Vertx
import io.vertx.redis.RedisClient
// If a config file is set, read the host and port.
def host = Vertx.currentContext().config().host
if (host == null) {
  host = "127.0.0.1"
}

// Create the redis client
def client = RedisClient.create(vertx, [
  host:host
])

client.set("key", "value", { r ->
  if (r.succeeded()) {
    println("key stored")
    client.get("key", { s ->
      println("Retrieved value: ${s.result()}")
    })
  } else {
    println("Connection or Operation Failed ${r.cause()}")
  }
})
