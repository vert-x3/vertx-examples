require 'vertx/record_parser'

$vertx.create_net_server().connect_handler() { |sock|

  parser = Vertx::RecordParser.new_delimited("\n", sock)

  parser.end_handler() { |v|
    sock.close()
  }.exception_handler() { |t|
    t.print_stack_trace()
    sock.close()
  }.handler() { |buffer|
    name = buffer.to_string("UTF-8")
    sock.write("Hello #{name}\n", "UTF-8")
  }

}.listen(1234)

puts "Echo server is now listening"

