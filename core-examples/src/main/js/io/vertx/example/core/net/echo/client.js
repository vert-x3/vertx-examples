vertx.createNetClient().connect(1234, "localhost", function (res, res_err) {

  if (res_err == null) {
    var socket = res;
    socket.handler(function (buffer) {
      console.log("Net client receiving: " + buffer.toString("UTF-8"));
    });

    // Now send some data
    for (var i = 0; i < 10; i++) {
      var str = "hello " + i + "
      ";
      console.log("Net client sending: " + str);
      socket.write(str);
    }
  } else {
    console.log("Failed to connect " + res_err);
  }
});
