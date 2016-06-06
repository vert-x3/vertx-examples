
# Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

options = {
  'ssl' => true,
  'useAlpn' => true,
  'sslEngine' => :OPENSSL,
  'protocolVersion' => :HTTP_2,
  'trustAll' => true
}

client = $vertx.create_http_client(options)

request = client.get(8443, "localhost", "/") { |resp|
  puts "Got response #{resp.status_code()} with protocol #{resp.version()}"
  resp.body_handler() { |body|
    puts "Got data #{body.to_string("ISO-8859-1")}"
  }
}

# Set handler for server side push
request.push_handler() { |pushedReq|
  puts "Receiving pushed content"
  pushedReq.handler() { |pushedResp|
    pushedResp.body_handler() { |body|
      puts "Got pushed data #{body.to_string("ISO-8859-1")}"
    }
  }
}

request.end()
