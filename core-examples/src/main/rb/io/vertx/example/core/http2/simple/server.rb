
server = $vertx.create_http_server({
  'useAlpn' => true,
  'sslEngineOptions' => {
  },
  'ssl' => true,
  'pemKeyCertOptions' => {
    'keyPath' => "server-key.pem",
    'certPath' => "server-cert.pem"
  }
})

server.request_handler() { |req|
  req.response().put_header("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1><p>version = #{req.version()}</p></body></html>")
}.listen(8443)
