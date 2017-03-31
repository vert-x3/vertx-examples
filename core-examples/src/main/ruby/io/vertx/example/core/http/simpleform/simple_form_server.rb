$vertx.create_http_server().request_handler() { |req|
  if (req.uri().==("/"))
    # Serve the index page
    req.response().send_file("index.html")
  elsif (req.uri().start_with?("/form"))
    req.response().set_chunked(true)
    req.set_expect_multipart(true)
    req.end_handler() { |v|
      req.form_attributes().names().each do |attr|
        req.response().write("Got attr #{attr} : #{req.form_attributes().get(attr)}\n")
      end
      req.response().end()
    }
  else
    req.response().set_status_code(404).end()
  end
}.listen(8080)
