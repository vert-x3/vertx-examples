require 'vertx-web-client/web_client'
require 'vertx/multi_map'

$vertx.create_http_server().request_handler() { |req|
  puts "Got form with content-type #{req.get_header("content-type")}"
  req.set_expect_multipart(true)
  req.end_handler() { |v|
    puts "firstName: #{req.get_form_attribute("firstName")}"
    puts "lastName: #{req.get_form_attribute("lastName")}"
    puts "male: #{req.get_form_attribute("male")}"
  }

}.listen(8080) { |listenResult_err,listenResult|
  if (listenResult_err != nil)
    puts "Could not start HTTP server"
    listenResult_err.print_stack_trace()
  else

    client = VertxWebClient::WebClient.create($vertx)

    form = Vertx::MultiMap.case_insensitive_multi_map()
    form.add("firstName", "Dale")
    form.add("lastName", "Cooper")
    form.add("male", "true")

    client.post(8080, "localhost", "/").put_header("content-type", "multipart/form-data").send_form(form) { |ar_err,ar|
      if (ar_err == nil)
        response = ar
        puts "Got HTTP response with status #{response.status_code()}"
      else
        ar_err.print_stack_trace()
      end
    }
  end
}
