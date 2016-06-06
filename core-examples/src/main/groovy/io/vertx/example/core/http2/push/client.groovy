import io.vertx.core.http.HttpVersion

// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

def options = [
  ssl:true,
  useAlpn:true,
  openSslEngineOptions:[:],
  protocolVersion:HttpVersion.HTTP_2,
  trustAll:true
]

def client = vertx.createHttpClient(options)

def request = client.get(8443, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})

// Set handler for server side push
request.pushHandler({ pushedReq ->
  println("Receiving pushed content")
  pushedReq.handler({ pushedResp ->
    pushedResp.bodyHandler({ body ->
      println("Got pushed data ${body.toString("ISO-8859-1")}")
    })
  })
})

request.end()
