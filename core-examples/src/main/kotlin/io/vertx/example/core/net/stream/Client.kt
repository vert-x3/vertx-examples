package io.vertx.example.core.net.stream

import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.core.json.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createNetClient().connect(1234, "localhost", { ar ->
      if (ar.succeeded()) {

        var socket = ar.result()

        // Create batch stream for reading and writing
        var batchStream = io.vertx.example.core.net.stream.BatchStream(socket, socket)

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
        var jsonObject = json {
          obj(
            "id" to java.util.UUID.randomUUID().toString(),
            "name" to "Vert.x",
            "timestamp" to java.time.Instant.now()
          )
        }

        // JsonArray
        var jsonArray = json {
          array(java.util.UUID.randomUUID().toString(), "Vert.x", java.time.Instant.now())
        }

        // Buffer
        var buffer = Buffer.buffer("Vert.x is awesome!")

        // Write to socket
        batchStream.write(io.vertx.example.core.net.stream.Batch(jsonObject))
        batchStream.write(io.vertx.example.core.net.stream.Batch(jsonArray))
        batchStream.write(io.vertx.example.core.net.stream.Batch(buffer))

      } else {
        println("Failed to connect ${ar.cause()}")
      }
    })
  }
}
