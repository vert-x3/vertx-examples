require 'vertx/pump'

$vertx.create_net_server().connect_handler() { |sock|

  # Create a pump
  Vertx::Pump.pump(sock, sock).start()

}.listen(1234)

puts "Echo server is now listening"

