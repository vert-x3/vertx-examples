eb = $vertx.event_bus()

# Send a message every second

$vertx.set_periodic(1000) { |v|

  eb.send("ping-address", "ping!") { |reply_err,reply|
    if (reply_err == nil)
      puts "Received reply #{reply.body()}"
    else
      puts "No reply"
    end
  }

}
