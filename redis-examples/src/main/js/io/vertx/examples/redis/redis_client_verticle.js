var Vertx = require("vertx-js/vertx");
var RedisClient = require("vertx-redis-js/redis_client");
// If a config file is set, read the host and port.
var host = Vertx.currentContext().config().host;
if ((host === null || host === undefined)) {
  host = "127.0.0.1";
}

// Create the redis client
var client = RedisClient.create(vertx, {
  "host" : host
});

client.set("key", "value", function (r, r_err) {
  if (r_err == null) {
    console.log("key stored");
    client.get("key", function (s, s_err) {
      console.log("Retrieved value: " + s);
    });
  } else {
    console.log("Connection or Operation Failed " + r_err);
  }
});
