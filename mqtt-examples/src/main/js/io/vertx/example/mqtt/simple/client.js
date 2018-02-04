var MqttClient = require("vertx-mqtt-js/mqtt_client");
var Buffer = require("vertx-js/buffer");
var MQTT_MESSAGE = "Hello Vert.x MQTT Client";
var BROKER_HOST = "localhost";
var BROKER_PORT = 1883;
var MQTT_TOPIC = "/my_topic";
var mqttClient = MqttClient.create(vertx);

mqttClient.connect(BROKER_PORT, BROKER_HOST, function (ch, ch_err) {
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
