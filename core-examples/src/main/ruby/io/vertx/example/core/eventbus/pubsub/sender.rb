
eb = $vertx.event_bus()

# Send a message every second

$vertx.set_periodic(1000) { |v|
  eb.publish("news-feed", "Some news!")
}
