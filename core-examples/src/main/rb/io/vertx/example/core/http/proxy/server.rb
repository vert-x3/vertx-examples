
$vertx.create_http_server().request_handler() { |req|

  puts "Got request #{req.uri()}"

  req.headers().names().each do |name|
    puts "#{name}: #{req.headers().get(name)}"
  end

  req.handler() { |data|
    puts "Got data #{data.to_string("ISO-8859-1")}"}

  req.end_handler() { |v|
    # Now send back a response
    req.response().set_chunked(true)

    i = 0
    while (i < 10)
      req.response().write("server-data-chunk-#{i}")
      i+=1
    end

    req.response().end()
  }
}.listen(8282)

