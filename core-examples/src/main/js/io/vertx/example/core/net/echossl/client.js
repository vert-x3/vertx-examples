
var options = {
  "ssl" : true,
  "trustAll" : true
};

vertx.createNetClient(options).connect(1234, "localhost", function (res, res_err) {
  if (res_err == null) {
    var sock = res;
    sock.handler(function (buff) {
      console.log("client receiving " + buff.toString("UTF-8"));
    });

    // Now send some data
    for (var i = 0;i < 10;i++) {
      var str = "hello " + i + "\n";
      console.log("Net client sending: " + str);
      sock.write(str);
    }
  } else {
    console.log("Failed to connect " + res_err);
  }
});
