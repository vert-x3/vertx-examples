require 'vertx/record_parser'
$vertx.create_net_client().connect(1234, "localhost") { |res_err,res|

  if (res_err == nil)
    socket = res

    Vertx::RecordParser.new_delimited("\n", socket).end_handler() { |v|
      socket.close()
    }.exception_handler() { |t|
      t.print_stack_trace()
      socket.close()
    }.handler() { |buffer|
      greeting = buffer.to_string("UTF-8")
      puts "Net client receiving: #{greeting}"
    }

    # Now send some data
    Java::JavaUtilStream::Stream.of("John", "Joe", "Lisa", "Bill").for_each(lambda { |name|
      puts "Net client sending: #{name}"
      socket.write(name).write("\n")
    })

  else
    puts "Failed to connect #{res_err}"
  end
}
