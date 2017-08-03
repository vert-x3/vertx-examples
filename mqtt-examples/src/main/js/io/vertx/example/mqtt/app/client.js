var Vertx = require("vertx-js/vertx");
var MqttClient = require("vertx-mqtt-server-js/mqtt_client");
var Buffer = require("vertx-js/buffer");
var SERVER_PORT = 1883;
var MQTT_MESSAGE = "Hello Vert.x MQTT Client";
var MQTT_TOPIC = "/my_topic";
var SERVER_HOST = "0.0.0.0";
var options = {
  "port" : SERVER_PORT,
  "host" : SERVER_HOST,
  "keepAliveTimeSeconds" : 2
};

var client = MqttClient.create(Vertx.vertx(), options);


// handler will be called when we have a message in topic we subscribing for
client.publishHandler(function (publish) {
  console.log("Just received message on [" + publish.topicName() + "] payload [" + publish.payload().toString(Java.type("java.nio.charset.Charset").defaultCharset()) + "] with QoS [" + publish.qosLevel() + "]");
});

// handle response on subscribe request
client.subscribeCompleteHandler(function (h) {
  console.log("Receive SUBACK from server with granted QoS : " + h.grantedQoSLevels());

  // let's publish a message to the subscribed topic
  client.publish(MQTT_TOPIC, Buffer.buffer(MQTT_MESSAGE), 'AT_MOST_ONCE', false, false, function (s, s_err) {
    console.log("Publish sent to a server");
  });

  // unsubscribe from receiving messages for earlier subscribed topic
  vertx.setTimer(5000, function (l) {
    client.unsubscribe(MQTT_TOPIC);
  });
});

// handle response on unsubscribe request
client.unsubscribeCompleteHandler(function (h) {
  console.log("Receive UNSUBACK from server");
  vertx.setTimer(5000, function (l) {
    client.disconnect(function (d, d_err) {
      console.log("Disconnected form server");
    });
  });
});

// connect to a server
client.connect(function (ch, ch_err) {
  if (ch_err == null) {
    console.log("Connected to a server");
    client.subscribe(MQTT_TOPIC, 0);
  } else {
    console.log("Failed to connect to a server");
    console.log(ch_err);
  }
});
