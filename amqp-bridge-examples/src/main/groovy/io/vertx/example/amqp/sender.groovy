import groovy.transform.Field
import io.vertx.groovy.amqp.bridge.AmqpBridge

def bridge = AmqpBridge.create(vertx)
@Field def count = 1

// Start the bridge, then use the event loop thread to process things thereafter.
bridge.start("localhost", 5672, { res ->
  if (!res.succeeded()) {
    println("Bridge startup failed: ${res.cause()}")
    return
  }

  // Set up a producer using the bridge, send a message with it.
  def producer = bridge.createProducer("myAmqpAddress")

  // Schedule sending of a message every second
  println("Producer created, scheduling sends.")
  vertx.setPeriodic(1000, { v ->
    def amqpMsgPayload = [ "body" : "myStringContent" + "${count}"]

    producer.send(amqpMsgPayload)

    println("Sent message: ${count++}")
  })
})
