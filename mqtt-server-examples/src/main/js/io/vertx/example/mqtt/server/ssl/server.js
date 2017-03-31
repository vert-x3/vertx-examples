var MqttServer = require("vertx-mqtt-server-js/mqtt_server");

var options = {
  "port" : 8883,
  "pemKeyCertOptions" : {
    "keyPath" : "server-key.pem",
    "certPath" : "server-cert.pem"
  },
  "ssl" : true
};

var mqttServer = MqttServer.create(vertx, options);

mqttServer.endpointHandler(function (endpoint) {

  // shows main connect info
  console.log("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, " + "clean session = " + endpoint.isCleanSession());

  // accept connection from the remote client
  endpoint.accept(false);

}).listen(function (ar, ar_err) {

  if (ar_err == null) {
    console.log("MQTT server is listening on port " + mqttServer.actualPort());
  } else {
    console.error("Error on starting the server" + ar_err.getMessage());
  }
});
