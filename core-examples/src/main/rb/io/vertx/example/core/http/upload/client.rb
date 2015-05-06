require 'vertx/pump'
req = $vertx.create_http_client({
}).put(8080, "localhost", "/someurl") { |resp|
  puts "Response #{resp.status_code()}"
}
filename = "upload.txt"
fs = $vertx.file_system()

fs.props(filename) { |ares,ares_err|
  props = ares
  puts "props is #{props}"
  size = props.size()
  req.headers().set("content-length", Java::JavaLang::String.value_of(size))
  fs.open(filename, {
  }) { |ares2,ares2_err|
    file = ares2
    pump = Vertx::Pump.pump(file, req)
    file.end_handler() { |v|
      req.end()
    }
    pump.start()
  }
}


