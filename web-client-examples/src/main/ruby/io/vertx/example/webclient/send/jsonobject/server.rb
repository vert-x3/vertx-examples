require 'json'

$vertx.create_http_server().request_handler() { |req|

  req.body_handler() { |buff|
    puts "Receiving user #{JSON.generate(buff.to_json_object())} from client "
    req.response().end()
  }

}.listen(8080) { |listenResult_err,listenResult|
  if (listenResult_err != nil)
    puts "Could not start HTTP server"
    listenResult_err.print_stack_trace()
  else
    puts "Server started"
  end
}
