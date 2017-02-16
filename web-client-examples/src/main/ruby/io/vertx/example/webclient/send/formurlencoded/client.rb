require 'vertx-web-client/web_client'
require 'vertx/multi_map'

client = VertxWebClient::WebClient.create($vertx)

form = Vertx::MultiMap.case_insensitive_multi_map()
form.add("firstName", "Dale")
form.add("lastName", "Cooper")
form.add("male", "true")

client.post(8080, "localhost", "/").send_form(form) { |ar_err,ar|
  if (ar_err == nil)
    response = ar
    puts "Got HTTP response with status #{response.status_code()}"
  else
    ar_err.print_stack_trace()
  end
}
