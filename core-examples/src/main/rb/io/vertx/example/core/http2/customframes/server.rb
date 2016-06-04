require 'vertx/buffer'

server = $vertx.create_http_server({
  'useAlpn' => true,
  'sslEngine' => :OPENSSL,
  'ssl' => true,
  'pemKeyCertOptions' => {
    'keyPath' => "server-key.pem",
    'certPath' => "server-cert.pem"
  }
})

server.request_handler() { |req|
  resp = req.response()

  req.custom_frame_handler() { |frame|
    puts "Received client frame #{frame.payload().to_string("UTF-8")}"

    # Write the sam
    resp.write_custom_frame(10, 0, Vertx::Buffer.buffer("pong"))
  }
}.listen(8443)
