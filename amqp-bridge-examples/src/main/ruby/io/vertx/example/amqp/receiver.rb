require 'vertx-amqp-bridge/amqp_bridge'

bridge = VertxAmqpBridge::AmqpBridge.create($vertx)

# Start the bridge, then use the event loop thread to process things thereafter.
bridge.start("localhost", 5672) { |res_err,res|
  if (res_err != nil)
    puts "Bridge startup failed: #{res_err}"
    return
  end

  # Set up a consumer using the bridge, register a handler for it.
  consumer = bridge.create_consumer("myAmqpAddress")
  consumer.handler() { |vertxMsg|
    amqpMsgPayload = vertxMsg.body()
    amqpBody = amqpMsgPayload["body"]

    # Print body of received AMQP message
    puts "Received a message with body: #{amqpBody}"
  }
}
