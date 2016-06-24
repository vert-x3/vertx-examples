
def server = vertx.createHttpServer([
  useAlpn:true,
  ssl:true,
  pemKeyCertOptions:[
    keyPath:"server-key.pem",
    certPath:"server-cert.pem"
  ]
])

server.requestHandler({ req ->
  req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1><p>version = ${req.version()}</p></body></html>")
}).listen(8443)
