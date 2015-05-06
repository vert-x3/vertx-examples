$vertx.create_http_server().websocket_handler() { |ws|
  ws.handler(&ws.method(:write_message))}.request_handler() { |req|
  if (req.uri().equals("/"))
    req.response().send_file("ws.html")end
}.listen(8080)
