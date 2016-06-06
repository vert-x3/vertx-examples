import io.vertx.groovy.core.buffer.Buffer

def server = vertx.createHttpServer([
  useAlpn:true,
  sslEngineOptions:[:],
  ssl:true,
  pemKeyCertOptions:[
    keyPath:"server-key.pem",
    certPath:"server-cert.pem"
  ]
])

server.requestHandler({ req ->
  def resp = req.response()

  req.customFrameHandler({ frame ->
    println("Received client frame ${frame.payload().toString("UTF-8")}")

    // Write the sam
    resp.writeCustomFrame(10, 0, Buffer.buffer("pong"))
  })
}).listen(8443)
