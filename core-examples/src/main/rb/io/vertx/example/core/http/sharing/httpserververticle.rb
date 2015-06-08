$vertx.create_http_server().request_handler() { |req|
  req.response().put_header("content-type", "text/html").end("<html><body><h1>Hello from #{self}</h1></body></html>")
}.listen(8080)
