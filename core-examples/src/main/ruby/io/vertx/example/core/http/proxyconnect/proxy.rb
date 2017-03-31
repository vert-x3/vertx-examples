
client = $vertx.create_net_client({
})

$vertx.create_http_server().request_handler() { |req|
  if (req.method() == :CONNECT)

    # Determine proxied server address
    proxyAddress = req.uri()
    idx = proxyAddress.index_of(':')
    host = proxyAddress.substring(0, idx)
    port = Java::JavaLang::Integer.parse_int(proxyAddress.substring(idx + 1))

    puts "Connecting to proxy #{proxyAddress}"
    client.connect(port, host) { |ar_err,ar|

      if (ar_err == nil)
        puts "Connected to proxy"
        clientSocket = req.net_socket()
        clientSocket.write("HTTP/1.0 200 Connection established\n\n")
        serverSocket = ar

        serverSocket.handler() { |buff|
          puts "Forwarding server packet to the client"
          clientSocket.write(buff)
        }
        serverSocket.close_handler() { |v|
          puts "Server socket closed"
          clientSocket.close()
        }

        clientSocket.handler() { |buff|
          puts "Forwarding client packet to the server"
          serverSocket.write(buff)
        }
        clientSocket.close_handler() { |v|
          puts "Client socket closed"
          serverSocket.close()
        }
      else

        puts "Fail proxy connection"
        req.response().set_status_code(403).end()
      end
    }
  else
    req.response().set_status_code(405).end()
  end
}.listen(8080)
