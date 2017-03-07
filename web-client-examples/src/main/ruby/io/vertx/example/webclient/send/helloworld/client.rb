require 'vertx-web-client/web_client'
require 'vertx/buffer'

client = VertxWebClient::WebClient.create($vertx)

body = Vertx::Buffer.buffer("Hello World")

client.put(8080, "localhost", "/").send_buffer(body) { |ar_err,ar|
  if (ar_err == nil)
    response = ar
    puts "Got HTTP response with status #{response.status_code()}"
  else
    ar_err.print_stack_trace()
  end
}
