var RecordParser = require("vertx-js/record_parser");

vertx.createNetServer().connectHandler(function (sock) {

  var parser = RecordParser.newDelimited("\n", sock);

  parser.endHandler(function (v) {
    sock.close();
  }).exceptionHandler(function (t) {
    t.printStackTrace();
    sock.close();
  }).handler(function (buffer) {
    var name = buffer.toString("UTF-8");
    sock.write("Hello " + name + "\n", "UTF-8");
  });

}).listen(1234);

console.log("Echo server is now listening");

