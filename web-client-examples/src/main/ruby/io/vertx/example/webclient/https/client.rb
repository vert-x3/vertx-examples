require 'vertx-web-client/web_client'

# Create the web client and enable SSL/TLS with a trust store
client = VertxWebClient::WebClient.create($vertx, {
  'ssl' => true,
  'trustStoreOptions' => {
    'path' => "client-truststore.jks",
    'password' => "wibble"
  }
})

client.get(8443, "localhost", "/").send() { |ar_err,ar|
  if (ar_err == nil)
    response = ar
    puts "Got HTTP response with status #{response.status_code()}"
  else
    ar_err.print_stack_trace()
  end
}
