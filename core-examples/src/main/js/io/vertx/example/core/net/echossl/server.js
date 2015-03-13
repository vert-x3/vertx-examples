var Pump = require("vertx-js/pump");

var options = {
  "ssl" : true,
  "keyStoreOptions" : {
    "path" : "server-keystore.jks",
    "password" : "wibble"
  }
};

vertx.createNetServer(options).connectHandler(function (sock) {

  // Create a pump
  Pump.pump(sock, sock).start();

}).listen(1234);

console.log("Echo server is now listening");
