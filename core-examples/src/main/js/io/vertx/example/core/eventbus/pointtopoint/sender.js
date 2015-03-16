var eb = vertx.eventBus();

// Send a message every second

vertx.setPeriodic(1000, function (v) {

  eb.send("ping-address", "ping!", function (reply, reply_err) {
    if (reply_err == null) {
      console.log("Received reply " + reply.body());
    } else {
      console.log("No reply");
    };
  });

});
