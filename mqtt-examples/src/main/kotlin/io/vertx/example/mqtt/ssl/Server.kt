package io.vertx.example.mqtt.ssl

import io.vertx.core.net.PemKeyCertOptions
import io.vertx.kotlin.core.net.*
import io.vertx.kotlin.mqtt.*
import io.vertx.mqtt.MqttServer
import io.vertx.mqtt.MqttServerOptions

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = MqttServerOptions(
      port = 8883,
      pemKeyCertOptions = PemKeyCertOptions(
        keyPath = "server-key.pem",
        certPath = "server-cert.pem"),
      ssl = true)

    var mqttServer = MqttServer.create(vertx, options)

    mqttServer.endpointHandler({ endpoint ->

      // shows main connect info
      println("MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession()}")

      // accept connection from the remote client
      endpoint.accept(false)

    }).listen({ ar ->

      if (ar.succeeded()) {
        println("MQTT server is listening on port ${mqttServer.actualPort()}")
      } else {
        System.err.println("Error on starting the server${ar.cause().getMessage()}")
      }
    })
  }
}
