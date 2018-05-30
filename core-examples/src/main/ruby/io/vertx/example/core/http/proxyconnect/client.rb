request = $vertx.create_http_client({
  'ssl' => true,
  'trustAll' => true,
  'verifyHost' => false,
  'proxyOptions' => {
    'type' => "HTTP",
    'host' => "localhost",
    'port' => 8080
  }
}).put(8282, "localhost", "/") { |resp|
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
