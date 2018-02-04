package io.vertx.example.core.net.stream


class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createNetServer().connectHandler({ socket ->

      // Create batch stream for reading and writing
      var batchStream = io.vertx.example.core.net.stream.BatchStream(socket, socket)

      // Pause reading data
      batchStream.pause()

      // Register read stream handler
      batchStream.handler({ batch ->

        // Print received batch object from the client
        println("Server Received : ${batch.getRaw().toString()}")

        // Write back batch object to the client
        batchStream.write(batch)

        // Check if write queue is full
        if (batchStream.writeQueueFull()) {

          // Pause reading data
          batchStream.pause()

          // Called once write queue is ready to accept more data
          batchStream.drainHandler({ done ->

            // Resume reading data
            batchStream.resume()

          })
        }
      }).endHandler({ v ->
        batchStream.end()
      }).exceptionHandler({ t ->
        t.printStackTrace()
        batchStream.end()
      })

      // Resume reading data
      batchStream.resume()

    }).listen(1234)
    println("Batch server is now listening to port : 1234")
  }
}
