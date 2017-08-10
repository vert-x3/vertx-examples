var MqttClient = require("vertx-mqtt-server-js/mqtt_client");
var Buffer = require("vertx-js/buffer");
var SERVER_PORT = 8883;
var MQTT_MESSAGE = "Hello Vert.x MQTT Client";
var MQTT_TOPIC = "/my_topic";
var SERVER_HOST = "0.0.0.0";
var options = {
};
options.port = SERVER_PORT;
options.host = SERVER_HOST;
options.ssl = true;
options.trustAll = true;

var mqttClient = MqttClient.create(vertx, options);

mqttClient.connect(function (ch, ch_err) {
  if (ch_err == null) {
    console.log("Connected to a server");

    mqttClient.publish(MQTT_TOPIC, Buffer.buffer(MQTT_MESSAGE), 'AT_MOST_ONCE', false, false, function (s, s_err) {
      mqttClient.disconnect(function (d, d_err) {
        console.log("Disconnected from server");
      });
    });
  } else {
    console.log("Failed to connect to a server");
    console.log(ch_err);
  }
});
