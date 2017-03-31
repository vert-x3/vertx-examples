require 'json'
require 'vertx-web-client/web_client'
require 'vertx-web-common/body_codec'

client = VertxWebClient::WebClient.create($vertx)

client.get(8080, "localhost", "/").as(VertxWebCommon::BodyCodec.json_object()).send() { |ar_err,ar|
  if (ar_err == nil)
    response = ar
    puts "Got HTTP response body"
    puts JSON.generate(response.body())
  else
    ar_err.print_stack_trace()
  end
}
