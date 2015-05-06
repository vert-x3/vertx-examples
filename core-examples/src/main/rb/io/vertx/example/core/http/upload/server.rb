require 'vertx/pump'
$vertx.create_http_server().request_handler() { |req|
  req.pause()
  filename = "#{Java::JavaUtil::UUID.random_uuid()}.uploaded"
  $vertx.file_system().open(filename, {
  }) { |ares,ares_err|
    file = ares
    pump = Vertx::Pump.pump(req, file)
    req.end_handler() { |v1|
      file.close() { |v2,v2_err|
        puts "Uploaded to #{filename}"
        req.response().end()
      }}
    pump.start()
    req.resume()
  }
}.listen(8080)
