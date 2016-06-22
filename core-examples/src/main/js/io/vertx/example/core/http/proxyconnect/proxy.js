
var client = vertx.createNetClient({
});

vertx.createHttpServer().requestHandler(function (req) {
  if (req.method() === 'CONNECT') {

    // Determine proxied server address
    var proxyAddress = req.uri();
    var idx = proxyAddress.indexOf(':');
    var host = proxyAddress.substring(0, idx);
    var port = Java.type("java.lang.Integer").parseInt(proxyAddress.substring(idx + 1));

    console.log("Connecting to proxy " + proxyAddress);
    client.connect(port, host, function (ar, ar_err) {

      if (ar_err == null) {
        console.log("Connected to proxy");
        var clientSocket = req.netSocket();
        clientSocket.write("HTTP/1.0 200 Connection established\n\n");
        var serverSocket = ar;

        serverSocket.handler(function (buff) {
          console.log("Forwarding server packet to the client");
          clientSocket.write(buff);
        });
        serverSocket.closeHandler(function (v) {
          console.log("Server socket closed");
          clientSocket.close();
        });

        clientSocket.handler(function (buff) {
          console.log("Forwarding client packet to the server");
          serverSocket.write(buff);
        });
        clientSocket.closeHandler(function (v) {
          console.log("Client socket closed");
          serverSocket.close();
        });
      } else {

        console.log("Fail proxy connection");
        req.response().setStatusCode(403).end();
      }
    });
  } else {
    req.response().setStatusCode(405).end();
  }
}).listen(8080);
