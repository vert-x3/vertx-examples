require 'vertx-web-client/web_client'

filename = "upload.txt"
fs = $vertx.file_system()

client = VertxWebClient::WebClient.create($vertx)

fs.props(filename) { |ares_err,ares|
  props = ares
  puts "props is #{props}"
  size = props.size()

  req = client.put(8080, "localhost", "/")
  req.put_header("content-length", "#{size}")

  fs.open(filename, {
  }) { |ares2_err,ares2|
    req.send_stream(ares2) { |ar_err,ar|
      if (ar_err == nil)
        response = ar
        puts "Got HTTP response with status #{response.status_code()}"
      else
        ar_err.print_stack_trace()
      end
    }
  }
}
