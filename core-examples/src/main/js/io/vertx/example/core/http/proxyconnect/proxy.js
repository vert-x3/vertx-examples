var client = vertx.createNetClient({
});
vertx.createHttpServer().requestHandler(function (req) {
  if (req.method() === 'CONNECT') {
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
          console.log("A");
          clientSocket.write(buff);
        });
        serverSocket.closeHandler(function (v) {
          console.log("B");
          clientSocket.close();
        });
        clientSocket.handler(function (buff) {
          console.log("C");
          serverSocket.write(buff);
        });
        clientSocket.closeHandler(function (v) {
          console.log("D");
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
