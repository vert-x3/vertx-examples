var Pump = require("vertx-js/pump");

vertx.createNetServer().connectHandler(function (sock) {

  // Create a pump
  Pump.pump(sock, sock).start();

}).listen(1234);

console.log("Echo server is now listening");

