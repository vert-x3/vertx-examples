var Buffer = require("vertx-js/buffer");

var server = vertx.createHttpServer({
  "useAlpn" : true,
  "sslEngine" : 'OPENSSL',
  "ssl" : true,
  "pemKeyCertOptions" : {
    "keyPath" : "server-key.pem",
    "certPath" : "server-cert.pem"
  }
});

server.requestHandler(function (req) {
  var resp = req.response();

  req.customFrameHandler(function (frame) {
    console.log("Received client frame " + frame.payload().toString("UTF-8"));

    // Write the sam
    resp.writeCustomFrame(10, 0, Buffer.buffer("pong"));
  });
}).listen(8443);
