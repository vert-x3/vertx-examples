var MqttServer = require("vertx-mqtt-js/mqtt_server");

var options = {
  "port" : 1883,
  "host" : "0.0.0.0"
};

var server = MqttServer.create(vertx, options);

server.endpointHandler(function (endpoint) {

  console.log("connected client " + endpoint.clientIdentifier());

  endpoint.publishHandler(function (message) {

    console.log("Just received message on [" + message.topicName() + "] payload [" + message.payload() + "] with QoS [" + message.qosLevel() + "]");
  });

  endpoint.accept(false);
});

server.listen(function (ar, ar_err) {
  if (ar_err == null) {
    console.log("MQTT server started and listening on port " + server.actualPort());
  } else {
    console.error("MQTT server error on start" + ar_err.getMessage());
  }
});
