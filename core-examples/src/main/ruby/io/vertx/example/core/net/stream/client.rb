require 'vertx/buffer'
$vertx.create_net_client().connect(1234, "localhost") { |ar_err,ar|
  if (ar_err == nil)

    socket = ar

    # Create batch stream for reading and writing
    batchStream = Java::IoVertxExampleCoreNetStream::BatchStream.new(socket, socket)

    # Pause reading data
    batchStream.pause()

    # Register read stream handler
    batchStream.handler() { |batch|
      puts "Client Received : #{batch.get_raw().to_string()}"
    }.end_handler() { |v|
      batchStream.end()
    }.exception_handler() { |t|
      t.print_stack_trace()
      batchStream.end()
    }

    # Resume reading data
    batchStream.resume()

    # JsonObject
    jsonObject = {
      'id' => Java::JavaUtil::UUID.random_uuid().to_string(),
      'name' => "Vert.x",
      'timestamp' => Java::JavaTime::Instant.now()
    }

    # JsonArray
    jsonArray = [
      Java::JavaUtil::UUID.random_uuid().to_string(),
      "Vert.x",
      Java::JavaTime::Instant.now()
    ]

    # Buffer
    buffer = Vertx::Buffer.buffer("Vert.x is awesome!")

    # Write to socket
    batchStream.write(Java::IoVertxExampleCoreNetStream::Batch.new(jsonObject))
    batchStream.write(Java::IoVertxExampleCoreNetStream::Batch.new(jsonArray))
    batchStream.write(Java::IoVertxExampleCoreNetStream::Batch.new(buffer))

  else
    puts "Failed to connect #{ar_err}"
  end
}
