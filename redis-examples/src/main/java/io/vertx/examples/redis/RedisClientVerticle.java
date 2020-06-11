package io.vertx.examples.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

import java.util.Arrays;

/**
 * A verticle setting and reading a value in Redis.
 */
public class RedisClientVerticle extends AbstractVerticle {

  @Override
  public void start() {
    // If a config file is set, read the host and port.
    String host = Vertx.currentContext().config().getString("host");
    if (host == null) {
      host = "127.0.0.1";
    }

    // Create the redis client
    Redis client = Redis.createClient(vertx, new RedisOptions().addConnectionString(host));
    RedisAPI redis = RedisAPI.api(client);

    client.connect()
      .compose(conn -> {
        return redis.set(Arrays.asList("key", "value"))
          .compose(v -> {
            System.out.println("key stored");
            return redis.get("key");
          });
      }).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("Retrieved value: " + ar.result());
      } else {
        System.out.println("Connection or Operation Failed " + ar.cause());
      }
    });
  }
}
