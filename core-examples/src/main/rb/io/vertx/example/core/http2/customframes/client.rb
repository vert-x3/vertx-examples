require 'vertx/buffer'

# Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

options = {
  'ssl' => true,
  'useAlpn' => true,
  'openSslEngineOptions' => {
  },
  'protocolVersion' => :HTTP_2,
  'trustAll' => true
}

request = $vertx.create_http_client(options).get(8443, "localhost", "/")

request.handler() { |resp|

  # Print custom frames received from server

  resp.custom_frame_handler() { |frame|
    puts "Got frame from server #{frame.payload().to_string("UTF-8")}"
  }
}

request.send_head() { |version|

  # Once head has been sent we can send custom frames

  $vertx.set_periodic(1000) { |timerID|

    puts "Sending ping frame to server"
    request.write_custom_frame(10, 0, Vertx::Buffer.buffer("ping"))
  }
}
