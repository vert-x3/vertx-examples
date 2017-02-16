require 'vertx-web-client/web_client'

client = VertxWebClient::WebClient.create($vertx)

client.get(8080, "localhost", "/").send() { |ar_err,ar|
  if (ar_err == nil)
    response = ar
    puts "Got HTTP response with status #{response.status_code()} with data #{response.body().to_string("ISO-8859-1")}"
  else
    ar_err.print_stack_trace()
  end
}
