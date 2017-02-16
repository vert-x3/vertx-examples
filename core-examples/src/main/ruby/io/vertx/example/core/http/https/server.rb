
server = $vertx.create_http_server({
  'ssl' => true,
  'keyStoreOptions' => {
    'path' => "server-keystore.jks",
    'password' => "wibble"
  }
})

server.request_handler() { |req|
  req.response().put_header("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>")
}.listen(4443)
