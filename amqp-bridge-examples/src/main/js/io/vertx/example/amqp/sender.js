var AmqpBridge = require("vertx-amqp-bridge-js/amqp_bridge");
var bridge = AmqpBridge.create(vertx);
var count = 1;

// Start the bridge, then use the event loop thread to process things thereafter.
bridge.start("localhost", 5672, function (res, res_err) {
  if (res_err !== null) {
    console.log("Bridge startup failed: " + res_err);
    return;
  }

  // Set up a producer using the bridge, send a message with it.
  var producer = bridge.createProducer("myAmqpAddress");

  // Schedule sending of a message every second
  console.log("Producer created, scheduling sends.");
  vertx.setPeriodic(1000, function (v) {
    var amqpMsgPayload = {
      "body" : "myStringContent" + count
    };

    producer.send(amqpMsgPayload);

    console.log("Sent message: " + count++);
  });
});
