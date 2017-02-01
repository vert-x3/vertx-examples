request = $vertx.create_http_client({
}).put(8080, "localhost", "/") { |resp|
  puts "Got response #{resp.status_code()}"
  resp.body_handler() { |body|
    puts "Got data #{body.to_string("ISO-8859-1")}"
  }
}

request.set_chunked(true)

(0...10).each do |i|
  request.write("client-chunk-#{i}")
end

request.end()
