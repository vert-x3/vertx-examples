$vertx.create_http_server().request_handler() { |req|
  name = Java::JavaLangManagement::ManagementFactory.get_runtime_mx_bean().get_name()
  req.response().end("Happily served by #{name}")
}.listen(8080)
