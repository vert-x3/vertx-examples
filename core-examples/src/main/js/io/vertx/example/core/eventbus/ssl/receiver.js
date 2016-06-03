
var eb = vertx.eventBus();

eb.consumer("ping-address", function (message) {

  console.log("Received message: " + message.body());
  // Now send back reply
  message.reply("pong!");
});

console.log("Receiver ready!");
