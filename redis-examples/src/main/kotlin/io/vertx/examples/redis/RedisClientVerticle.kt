package io.vertx.examples.redis

import io.vertx.core.Vertx
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.redis.*
import io.vertx.redis.RedisClient
import io.vertx.redis.RedisOptions

class RedisClientVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    // If a config file is set, read the host and port.
    var host = Vertx.currentContext().config().getString("host")
    if (host == null) {
      host = "127.0.0.1"
    }

    // Create the redis client
    var client = RedisClient.create(vertx, RedisOptions(
      host = host))

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
  }
}
