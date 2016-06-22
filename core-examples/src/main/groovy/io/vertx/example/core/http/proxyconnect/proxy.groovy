import io.vertx.core.http.HttpMethod

def client = vertx.createNetClient([:])

vertx.createHttpServer().requestHandler({ req ->
  if (req.method() == HttpMethod.CONNECT) {

    // Determine proxied server address
    def proxyAddress = req.uri()
    def idx = proxyAddress.indexOf(':')
    def host = proxyAddress.substring(0, idx)
    def port = java.lang.Integer.parseInt(proxyAddress.substring(idx + 1))

    println("Connecting to proxy ${proxyAddress}")
    client.connect(port, host, { ar ->

      if (ar.succeeded()) {
        println("Connected to proxy")
        def clientSocket = req.netSocket()
        clientSocket.write("HTTP/1.0 200 Connection established\n\n")
        def serverSocket = ar.result()

        serverSocket.handler({ buff ->
          println("Forwarding server packet to the client")
          clientSocket.write(buff)
        })
        serverSocket.closeHandler({ v ->
          println("Server socket closed")
          clientSocket.close()
        })

        clientSocket.handler({ buff ->
          println("Forwarding client packet to the server")
          serverSocket.write(buff)
        })
        clientSocket.closeHandler({ v ->
          println("Client socket closed")
          serverSocket.close()
        })
      } else {

        println("Fail proxy connection")
        req.response().setStatusCode(403).end()
      }
    })
  } else {
    req.response().setStatusCode(405).end()
  }
}).listen(8080)
