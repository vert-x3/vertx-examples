
var eb = vertx.eventBus();

// Send a message every second

vertx.setPeriodic(1000, function (v) {
  eb.publish("news-feed", "Some news!");
});
