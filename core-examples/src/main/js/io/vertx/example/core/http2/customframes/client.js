var Buffer = require("vertx-js/buffer");

// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

var options = {
  "ssl" : true,
  "useAlpn" : true,
  "openSslEngineOptions" : {
  },
  "protocolVersion" : 'HTTP_2',
  "trustAll" : true
};

var request = vertx.createHttpClient(options).get(8443, "localhost", "/");

request.handler(function (resp) {

  // Print custom frames received from server

  resp.customFrameHandler(function (frame) {
    console.log("Got frame from server " + frame.payload().toString("UTF-8"));
  });
});

request.sendHead(function (version) {

  // Once head has been sent we can send custom frames

  vertx.setPeriodic(1000, function (timerID) {

    console.log("Sending ping frame to server");
    request.writeCustomFrame(10, 0, Buffer.buffer("ping"));
  });
});
