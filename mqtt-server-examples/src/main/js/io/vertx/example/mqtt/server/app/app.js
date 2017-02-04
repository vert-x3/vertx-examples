var MqttServer = require("vertx-mqtt-server-js/mqtt_server");
var Buffer = require("vertx-js/buffer");

var mqttServer = MqttServer.create(vertx);

mqttServer.endpointHandler(function (endpoint) {

  // shows main connect info
  console.log("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());

  if (endpoint.auth() !== null &&endpoint.auth() !== undefined) {
    console.log("[username = " + endpoint.auth().userName() + ", password = " + endpoint.auth().password() + "]");
  }
  if (endpoint.will() !== null &&endpoint.will() !== undefined) {
    console.log("[will flag = " + endpoint.will().isWillFlag() + " topic = " + endpoint.will().willTopic() + " msg = " + endpoint.will().willMessage() + " QoS = " + endpoint.will().willQos() + " isRetain = " + endpoint.will().isWillRetain() + "]");
  }

  console.log("[keep alive timeout = " + endpoint.keepAliveTimeSeconds() + "]");

  // accept connection from the remote client
  endpoint.accept(false);

  // handling requests for subscriptions
  endpoint.subscribeHandler(function (subscribe) {

    var grantedQosLevels = [];
    Array.prototype.forEach.call(subscribe.topicSubscriptions(), function(s) {
      console.log("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
      grantedQosLevels.push(s.qualityOfService().value());
    });
    // ack the subscriptions request
    endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

    // just as example, publish a message on the first topic with requested QoS
    endpoint.publish(subscribe.topicSubscriptions()[0].topicName(), Buffer.buffer("Hello from the Vert.x MQTT server"), subscribe.topicSubscriptions()[0].qualityOfService(), false, false);

    // specifing handlers for handling QoS 1 and 2
    endpoint.publishAcknowledgeHandler(function (messageId) {

      console.log("Received ack for message = " + messageId);

    }).publishReceivedHandler(function (messageId) {

      endpoint.publishRelease(messageId);

    }).publishCompleteHandler(function (messageId) {

      console.log("Received ack for message = " + messageId);
    });
  });

  // handling requests for unsubscriptions
  endpoint.unsubscribeHandler(function (unsubscribe) {

    Array.prototype.forEach.call(unsubscribe.topics(), function(t) {
      console.log("Unsubscription for " + t);
    });
    // ack the subscriptions request
    endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
  });

  // handling ping from client
  endpoint.pingHandler(function (v) {

    console.log("Ping received from client");
  });

  // handling disconnect message
  endpoint.disconnectHandler(function (v) {

    console.log("Received disconnect from client");
  });

  // handling closing connection
  endpoint.closeHandler(function (v) {

    console.log("Connection closed");
  });

  // handling incoming published messages
  endpoint.publishHandler(function (message) {

    console.log("Just received message on [" + message.topicName() + "] payload [" + message.payload() + "] with QoS [" + message.qosLevel() + "]");

    if (message.qosLevel() === 'AT_LEAST_ONCE') {
      endpoint.publishAcknowledge(message.messageId());
    } else if (message.qosLevel() === 'EXACTLY_ONCE') {
      endpoint.publishReceived(message.messageId());
    }

  }).publishReleaseHandler(function (messageId) {
    endpoint.publishComplete(messageId);
  });
}).listen(function (ar, ar_err) {

  if (ar_err == null) {
    console.log("MQTT server is listening on port " + mqttServer.actualPort());
  } else {
    console.error("Error on starting the server" + ar_err.getMessage());
  }
});
