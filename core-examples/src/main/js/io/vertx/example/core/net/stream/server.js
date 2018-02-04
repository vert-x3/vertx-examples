vertx.createNetServer().connectHandler(function (socket) {

  // Create batch stream for reading and writing
  var batchStream = new (Java.type("io.vertx.example.core.net.stream.BatchStream"))(socket, socket);

  // Pause reading data
  batchStream.pause();

  // Register read stream handler
  batchStream.handler(function (batch) {

    // Print received batch object from the client
    console.log("Server Received : " + batch.getRaw().toString());

    // Write back batch object to the client
    batchStream.write(batch);

    // Check if write queue is full
    if (batchStream.writeQueueFull()) {

      // Pause reading data
      batchStream.pause();

      // Called once write queue is ready to accept more data
      batchStream.drainHandler(function (done) {

        // Resume reading data
        batchStream.resume();

      });
    }
  }).endHandler(function (v) {
    batchStream.end();
  }).exceptionHandler(function (t) {
    t.printStackTrace();
    batchStream.end();
  });

  // Resume reading data
  batchStream.resume();

}).listen(1234);
console.log("Batch server is now listening to port : 1234");
