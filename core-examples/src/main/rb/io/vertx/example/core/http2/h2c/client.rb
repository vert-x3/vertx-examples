
options = {
  'protocolVersion' => "HTTP_2"
}

$vertx.create_http_client(options).get_now(8080, "localhost", "/") { |resp|
  puts "Got response #{resp.status_code()} with protocol #{resp.version()}"
  resp.body_handler() { |body|
    puts "Got data #{body.to_string("ISO-8859-1")}"
  }
}
