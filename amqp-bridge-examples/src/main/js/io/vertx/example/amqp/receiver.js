var AmqpBridge = require("vertx-amqp-bridge-js/amqp_bridge");
var bridge = AmqpBridge.create(vertx);

// Start the bridge, then use the event loop thread to process things thereafter.
bridge.start("localhost", 5672, function (res, res_err) {
  if (res_err !== null) {
    console.log("Bridge startup failed: " + res_err);
    return;
  }

  // Set up a consumer using the bridge, register a handler for it.
  var consumer = bridge.createConsumer("myAmqpAddress");
  consumer.handler(function (vertxMsg) {
    var amqpMsgPayload = vertxMsg.body();
    var amqpBody = amqpMsgPayload.body;

    // Print body of received AMQP message
    console.log("Received a message with body: " + amqpBody);
  });
});
