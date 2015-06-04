$vertx.create_http_server().request_handler() { |request|
    request.response().end("A ruby world full of gems")
}.listen(8080)
