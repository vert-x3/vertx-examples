
# Start an SSL/TLS http server
$vertx.create_http_server({
  'keyStoreOptions' => {
    'path' => "server-keystore.jks",
    'password' => "wibble"
  },
  'ssl' => true
}).request_handler() { |req|

  req.response().end()

}.listen(8443) { |listenResult_err,listenResult|
  if (listenResult_err != nil)
    puts "Could not start HTTP server"
    listenResult_err.print_stack_trace()
  else
    puts "Server started"
  end
}
