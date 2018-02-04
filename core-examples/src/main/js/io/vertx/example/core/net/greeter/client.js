var RecordParser = require("vertx-js/record_parser");
vertx.createNetClient().connect(1234, "localhost", function (res, res_err) {

  if (res_err == null) {
    var socket = res;

    RecordParser.newDelimited("\n", socket).endHandler(function (v) {
      socket.close();
    }).exceptionHandler(function (t) {
      t.printStackTrace();
      socket.close();
    }).handler(function (buffer) {
      var greeting = buffer.toString("UTF-8");
      console.log("Net client receiving: " + greeting);
    });

    // Now send some data
    Java.type("java.util.stream.Stream").of("John", "Joe", "Lisa", "Bill").forEach(function (name) {
      console.log("Net client sending: " + name);
      socket.write(name).write("\n");
    });

  } else {
    console.log("Failed to connect " + res_err);
  }
});
