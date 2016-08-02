import io.vertx.core.http.HttpMethod

def server = vertx.createHttpServer([
  useAlpn:true,
  ssl:true,
  pemKeyCertOptions:[
    keyPath:"server-key.pem",
    certPath:"server-cert.pem"
  ]
])

server.requestHandler({ req ->
  def path = req.path()
  def resp = req.response()
  if ("/" == path) {
    resp.push(HttpMethod.GET, "/script.js", { ar ->
      if (ar.succeeded()) {
        println("sending push")
        def pushedResp = ar.result()
        pushedResp.sendFile("script.js")
      } else {
        // Sometimes Safari forbids push : "Server push not allowed to opposite endpoint."
      }
    })

    resp.sendFile("index.html")
  } else if ("/script.js" == path) {
    resp.sendFile("script.js")
  } else {
    println("Not found ${path}")
    resp.setStatusCode(404).end()
  }
})

server.listen(8443, "localhost", { ar ->
  if (ar.succeeded()) {
    println("Server started")
  } else {
    ar.cause().printStackTrace()
  }
})
