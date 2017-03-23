require 'vertx/buffer'
client = $vertx.create_http_client()

client.websocket(8080, "localhost", "/some-uri") { |websocket|
  websocket.handler() { |data|
    puts "Received data #{data.to_string("ISO-8859-1")}"
    client.close()
  }
  websocket.write_binary_message(Vertx::Buffer.buffer("Hello world"))
}
