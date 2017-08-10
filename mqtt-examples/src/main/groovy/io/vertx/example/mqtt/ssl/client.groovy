import groovy.transform.Field
import io.vertx.mqtt.MqttClient
import io.vertx.core.buffer.Buffer
import io.netty.handler.codec.mqtt.MqttQoS
@Field def SERVER_PORT = 8883
@Field def MQTT_MESSAGE = "Hello Vert.x MQTT Client"
@Field def MQTT_TOPIC = "/my_topic"
@Field def SERVER_HOST = "0.0.0.0"
def options = [:]
options.port = SERVER_PORT
options.host = SERVER_HOST
options.ssl = true
options.trustAll = true

def mqttClient = MqttClient.create(vertx, options)

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
