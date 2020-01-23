$vertx.create_http_server().web_socket_handler() { |ws|
  ws.handler(&ws.method(:write_binary_message))
}.request_handler() { |req|
  if (req.uri().==("/"))
    req.response().send_file("ws.html")end
}.listen(8080)
