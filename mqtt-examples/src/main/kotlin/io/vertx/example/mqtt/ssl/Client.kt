package io.vertx.example.mqtt.ssl

import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.mqtt.*
import io.vertx.mqtt.MqttClient
import io.vertx.mqtt.MqttClientOptions

class Client : io.vertx.core.AbstractVerticle()  {
  var SERVER_PORT = 8883
  var MQTT_MESSAGE = "Hello Vert.x MQTT Client"
  var MQTT_TOPIC = "/my_topic"
  var SERVER_HOST = "0.0.0.0"
  override fun start() {
    var options = MqttClientOptions()
    options.port = SERVER_PORT
    options.host = SERVER_HOST
    options.ssl = true
    options.trustAll = true

    var mqttClient = MqttClient.create(vertx, options)

    mqttClient.connect({ ch ->
      if (ch.succeeded()) {
        println("Connected to a server")

        mqttClient.publish(MQTT_TOPIC, Buffer.buffer(MQTT_MESSAGE), MqttQoS.AT_MOST_ONCE, false, false, { s ->
          mqttClient.disconnect({ d ->
            println("Disconnected from server")
          })
        })
      } else {
        println("Failed to connect to a server")
        println(ch.cause())
      }
    })
  }
}
