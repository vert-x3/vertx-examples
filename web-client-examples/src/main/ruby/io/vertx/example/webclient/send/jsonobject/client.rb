require 'vertx-web-client/web_client'

client = VertxWebClient::WebClient.create($vertx)

user = {
  'firstName' => "Date",
  'lastName' => "Cooper",
  'male' => true
}

client.put(8080, "localhost", "/").send_json(user) { |ar_err,ar|
  if (ar_err == nil)
    response = ar
    puts "Got HTTP response with status #{response.status_code()}"
  else
    ar_err.print_stack_trace()
  end
}
