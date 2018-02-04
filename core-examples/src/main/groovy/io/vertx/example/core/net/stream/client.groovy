import io.vertx.core.buffer.Buffer
vertx.createNetClient().connect(1234, "localhost", { ar ->
  if (ar.succeeded()) {

    def socket = ar.result()

    // Create batch stream for reading and writing
    def batchStream = new io.vertx.example.core.net.stream.BatchStream(socket, socket)

    // Pause reading data
    batchStream.pause()

    // Register read stream handler
    batchStream.handler({ batch ->
      println("Client Received : ${batch.getRaw().toString()}")
    }).endHandler({ v ->
      batchStream.end()
    }).exceptionHandler({ t ->
      t.printStackTrace()
      batchStream.end()
    })

    // Resume reading data
    batchStream.resume()

    // JsonObject
    def jsonObject = [
      id:java.util.UUID.randomUUID().toString(),
      name:"Vert.x",
      timestamp:java.time.Instant.now()
    ]

    // JsonArray
    def jsonArray = [
      java.util.UUID.randomUUID().toString(),
      "Vert.x",
      java.time.Instant.now()
    ]

    // Buffer
    def buffer = Buffer.buffer("Vert.x is awesome!")

    // Write to socket
    batchStream.write(new io.vertx.example.core.net.stream.Batch(jsonObject))
    batchStream.write(new io.vertx.example.core.net.stream.Batch(jsonArray))
    batchStream.write(new io.vertx.example.core.net.stream.Batch(buffer))

  } else {
    println("Failed to connect ${ar.cause()}")
  }
})
