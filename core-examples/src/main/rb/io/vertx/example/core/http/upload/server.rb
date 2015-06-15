require 'vertx/pump'
$vertx.create_http_server().request_handler() { |req|
  req.pause()
  filename = "#{Java::JavaUtil::UUID.random_uuid()}.uploaded"
  $vertx.file_system().open(filename, {
  }) { |ares_err,ares|
    file = ares
    pump = Vertx::Pump.pump(req, file)
    req.end_handler() { |v1|
      file.close() { |v2_err,v2|
        puts "Uploaded to #{filename}"
        req.response().end()
      }
    }
    pump.start()
    req.resume()
  }
}.listen(8080)
