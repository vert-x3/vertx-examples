package io.vertx.examples.redis;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

import java.util.Arrays;

/**
 * A verticle setting and reading a value in Redis.
 */
public class RedisClientVerticle extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{RedisClientVerticle.class.getName()});
  }

  private RedisAPI redis;

  @Override
  public Future<?> start() {
    // If a config file is set, read the host and port.
    String host = config().getString("host");
    if (host == null) {
      host = "127.0.0.1";
    }

    // Create the redis client
    Redis client = Redis.createClient(vertx, new RedisOptions().addConnectionString(host));
    redis = RedisAPI.api(client);

    return client.connect()
      .compose(conn ->
        redis.set(Arrays.asList("key", "value"))
          .compose(v -> {
            System.out.println("key stored");
            return redis.get("key");
          }))
      .onSuccess(result -> {
        System.out.println("Retrieved value: " + result);
      });
  }
}
