require 'vertx-amqp-bridge/amqp_bridge'

bridge = VertxAmqpBridge::AmqpBridge.create($vertx)
@count = 1

# Start the bridge, then use the event loop thread to process things thereafter.
bridge.start("localhost", 5672) { |res_err,res|
  if (res_err != nil)
    puts "Bridge startup failed: #{res_err}"
    return
  end

  # Set up a producer using the bridge, send a message with it.
  producer = bridge.create_producer("myAmqpAddress")

  # Schedule sending of a message every second
  puts "Producer created, scheduling sends."
  $vertx.set_periodic(1000) { |v|
    amqpMsgPayload = { "body" => "myStringContent#{@count}" }

    producer.send(amqpMsgPayload)

    puts "Sent message: #{@count}"
    @count+=1
  }
}
