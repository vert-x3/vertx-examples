
# Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

options = {
  'ssl' => true,
  'useAlpn' => true,
  'sslEngine' => :OPENSSL,
  'protocolVersion' => :HTTP_2,
  'trustAll' => true
}

$vertx.create_http_client(options).get_now(8443, "localhost", "/") { |resp|
  puts "Got response #{resp.status_code()} with protocol #{resp.version()}"
  resp.body_handler() { |body|
    puts "Got data #{body.to_string("ISO-8859-1")}"
  }
}
