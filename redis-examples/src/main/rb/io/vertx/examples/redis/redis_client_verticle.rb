require 'vertx-redis/redis_client'
# If a config file is set, read the host and port.
host = Java::IoVertxGroovyCore::Vertx.current_context().config()["host"]
if (host == nil)
  host = "127.0.0.1"
end

# Create the redis client
client = VertxRedis::RedisClient.create($vertx, {
  'host' => host
})

client.set("key", "value") { |r_err,r|
  if (r_err == nil)
    puts "key stored"
    client.get("key") { |s_err,s|
      puts "Retrieved value :#{s}"
    }
  else
    puts "Connection or Operation Failed #{r_err}"
  end
}
