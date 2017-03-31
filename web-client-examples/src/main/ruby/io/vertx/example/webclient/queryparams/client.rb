require 'vertx-web-client/web_client'

client = VertxWebClient::WebClient.create($vertx)

client.get(8080, "localhost", "/").add_query_param("firstName", "Dale").add_query_param("lastName", "Cooper").add_query_param("male", "true").send() { |ar_err,ar|
  if (ar_err == nil)
    response = ar
    puts "Got HTTP response with status #{response.status_code()}"
  else
    ar_err.print_stack_trace()
  end
}
