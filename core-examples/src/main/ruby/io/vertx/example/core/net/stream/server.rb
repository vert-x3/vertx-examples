$vertx.create_net_server().connect_handler() { |socket|

  # Create batch stream for reading and writing
  batchStream = Java::IoVertxExampleCoreNetStream::BatchStream.new(socket, socket)

  # Pause reading data
  batchStream.pause()

  # Register read stream handler
  batchStream.handler() { |batch|

    # Print received batch object from the client
    puts "Server Received : #{batch.get_raw().to_string()}"

    # Write back batch object to the client
    batchStream.write(batch)

    # Check if write queue is full
    if (batchStream.write_queue_full?())

      # Pause reading data
      batchStream.pause()

      # Called once write queue is ready to accept more data
      batchStream.drain_handler() { |done|

        # Resume reading data
        batchStream.resume()

      }
    end
  }.end_handler() { |v|
    batchStream.end()
  }.exception_handler() { |t|
    t.print_stack_trace()
    batchStream.end()
  }

  # Resume reading data
  batchStream.resume()

}.listen(1234)
puts "Batch server is now listening to port : 1234"
