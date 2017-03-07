$vertx.create_http_client().get_now(8080, "localhost", "/") { |resp|
  puts "Got response #{resp.status_code()}"
  resp.body_handler() { |body|
    puts "Got data #{body.to_string("ISO-8859-1")}"
  }
}
