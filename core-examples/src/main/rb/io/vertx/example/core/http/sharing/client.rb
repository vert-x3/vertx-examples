$vertx.set_periodic(1000) { |l|
  $vertx.create_http_client().get_now(8080, "localhost", "/") { |resp|
    resp.body_handler() { |body|
      puts body.to_string("ISO-8859-1")
    }
  }
}
