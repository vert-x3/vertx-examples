var Buffer = require("vertx-js/buffer");
var client = vertx.createHttpClient();

client.websocket(8080, "localhost", "/some-uri", function (websocket) {
  websocket.handler(function (data) {
    console.log("Received data " + data.toString("ISO-8859-1"));
    client.close();
  });
  websocket.writeBinaryMessage(Buffer.buffer("Hello world"));
});
