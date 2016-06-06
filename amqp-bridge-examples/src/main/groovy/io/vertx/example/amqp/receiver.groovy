import io.vertx.groovy.amqp.bridge.AmqpBridge

def bridge = AmqpBridge.create(vertx)

// Start the bridge, then use the event loop thread to process things thereafter.
bridge.start("localhost", 5672, { res ->
  if (!res.succeeded()) {
    println("Bridge startup failed: ${res.cause()}")
    return
  }

  // Set up a consumer using the bridge, register a handler for it.
  def consumer = bridge.createConsumer("myAmqpAddress")
  consumer.handler({ vertxMsg ->
    def amqpMsgPayload = vertxMsg.body()
    def amqpBody = amqpMsgPayload.body

    // Print body of received AMQP message
    println("Received a message with body: ${amqpBody}")
  })
})
