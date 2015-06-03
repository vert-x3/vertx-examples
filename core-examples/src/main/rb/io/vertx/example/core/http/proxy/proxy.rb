client = $vertx.create_http_client({
})
$vertx.create_http_server().request_handler() { |req|
  puts "Proxying request: #{req.uri()}"
  c_req = client.request(req.method(), 8282, "localhost", req.uri()) { |c_res|
    puts "Proxying response: #{c_res.status_code()}"
    req.response().set_chunked(true)
    req.response().set_status_code(c_res.status_code())
    req.response().headers().set_all(c_res.headers())
    c_res.handler() { |data|
      puts "Proxying response body: #{data.to_string("ISO-8859-1")}"
      req.response().write(data)
    }
    c_res.end_handler() { |v|
      req.response().end();
    }
  }
  c_req.set_chunked(true)
  c_req.headers().set_all(req.headers())
  req.handler() { |data|
    puts "Proxying request body #{data.to_string("ISO-8859-1")}"
    c_req.write(data)
  }
  req.end_handler() { |v|
    c_req.end();
  }
}.listen(8080)
