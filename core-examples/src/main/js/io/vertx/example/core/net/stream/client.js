var Buffer = require("vertx-js/buffer");
vertx.createNetClient().connect(1234, "localhost", function (ar, ar_err) {
  if (ar_err == null) {

    var socket = ar;

    // Create batch stream for reading and writing
    var batchStream = new (Java.type("io.vertx.example.core.net.stream.BatchStream"))(socket, socket);

    // Pause reading data
    batchStream.pause();

    // Register read stream handler
    batchStream.handler(function (batch) {
      console.log("Client Received : " + batch.getRaw().toString());
    }).endHandler(function (v) {
      batchStream.end();
    }).exceptionHandler(function (t) {
      t.printStackTrace();
      batchStream.end();
    });

    // Resume reading data
    batchStream.resume();

    // JsonObject
    var jsonObject = {
      "id" : Java.type("java.util.UUID").randomUUID().toString(),
      "name" : "Vert.x",
      "timestamp" : Java.type("java.time.Instant").now()
    };

    // JsonArray
    var jsonArray = [
      Java.type("java.util.UUID").randomUUID().toString(),
      "Vert.x",
      Java.type("java.time.Instant").now()
    ];

    // Buffer
    var buffer = Buffer.buffer("Vert.x is awesome!");

    // Write to socket
    batchStream.write(new (Java.type("io.vertx.example.core.net.stream.Batch"))(jsonObject));
    batchStream.write(new (Java.type("io.vertx.example.core.net.stream.Batch"))(jsonArray));
    batchStream.write(new (Java.type("io.vertx.example.core.net.stream.Batch"))(buffer));

  } else {
    console.log("Failed to connect " + ar_err);
  }
});
