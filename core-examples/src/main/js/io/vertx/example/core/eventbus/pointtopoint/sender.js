var eb = vertx.eventBus();

// Send a message every second

vertx.setPeriodic(1000, function (v) {

  eb.send("ping-address", "ping!", function (reply, reply_err) {
    console.log("Received reply " + reply.body());
  });

});
