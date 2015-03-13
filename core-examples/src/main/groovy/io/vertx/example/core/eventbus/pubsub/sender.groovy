
def eb = vertx.eventBus()

// Send a message every second

vertx.setPeriodic(1000, { v ->
  eb.publish("news-feed", "Some news!")})
