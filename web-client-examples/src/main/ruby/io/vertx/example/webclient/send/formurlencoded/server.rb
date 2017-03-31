
$vertx.create_http_server().request_handler() { |req|
  puts "Got form with content-type #{req.get_header("content-type")}"
  req.set_expect_multipart(true)
  req.end_handler() { |v|
    puts "firstName: #{req.get_form_attribute("firstName")}"
    puts "lastName: #{req.get_form_attribute("lastName")}"
    puts "male: #{req.get_form_attribute("male")}"

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
