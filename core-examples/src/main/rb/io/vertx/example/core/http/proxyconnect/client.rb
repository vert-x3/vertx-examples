request = $vertx.create_http_client({
  'proxyOptions' => {
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

i = 0
while (i < 10)
  request.write("client-chunk-#{i}")
  i+=1
end

request.end()
