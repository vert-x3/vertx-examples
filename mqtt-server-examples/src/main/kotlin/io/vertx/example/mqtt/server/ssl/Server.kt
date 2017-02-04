package io.vertx.example.mqtt.server.ssl

import io.vertx.kotlin.common.json.*
import io.vertx.mqtt.MqttServer

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = io.vertx.mqtt.MqttServerOptions(
      port = 8883,
      pemKeyCertOptions = io.vertx.core.net.PemKeyCertOptions(
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
