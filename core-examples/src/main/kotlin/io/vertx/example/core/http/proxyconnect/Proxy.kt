package io.vertx.example.core.http.proxyconnect

import io.vertx.core.http.HttpMethod
import io.vertx.core.net.NetClientOptions
import io.vertx.kotlin.core.net.*

class Proxy : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = vertx.createNetClient(NetClientOptions())

    vertx.createHttpServer().requestHandler({ req ->
      if (req.method() == HttpMethod.CONNECT) {

        // Determine proxied server address
        var proxyAddress = req.uri()
        var idx = proxyAddress.indexOf(':')
        var host = proxyAddress.substring(0, idx)
        var port = java.lang.Integer.parseInt(proxyAddress.substring(idx + 1))

        println("Connecting to proxy ${proxyAddress}")
        client.connect(port, host, { ar ->

          if (ar.succeeded()) {
            println("Connected to proxy")
            var clientSocket = req.netSocket()
            clientSocket.write("HTTP/1.0 200 Connection established\n\n")
            var serverSocket = ar.result()

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
  }
}
