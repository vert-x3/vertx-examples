package io.vertx.examples.redis

import io.vertx.core.Vertx
import io.vertx.kotlin.redis.client.*
import io.vertx.redis.client.Redis
import io.vertx.redis.client.RedisAPI
import io.vertx.redis.client.RedisOptions

class RedisClientVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    // If a config file is set, read the host and port.
    var host = Vertx.currentContext().config().getString("host")
    if (host == null) {
      host = "127.0.0.1"
    }

    // Create the redis client
    var client = Redis.createClient(vertx, RedisOptions(
      connectionStrings = listOf(host)))
    var redis = RedisAPI.api(client)

    client.connect().compose<Any>({ conn ->
      return redis.set(listOf("key", "value")).compose<Any>({ v ->
        println("key stored")
        return redis.get("key")
      })
    }).onComplete({ ar ->
      if (ar.succeeded()) {
        println("Retrieved value: ${ar.result()}")
      } else {
        println("Connection or Operation Failed ${ar.cause()}")
      }
    })
  }
}
