require 'vertx/pump'

options = {
  'ssl' => true,
  'keyStoreOptions' => {
    'path' => "server-keystore.jks",
    'password' => "wibble"
  }
}

$vertx.create_net_server(options).connect_handler() { |sock|

  # Create a pump
  Vertx::Pump.pump(sock, sock).start()

}.listen(1234)

puts "Echo server is now listening"
