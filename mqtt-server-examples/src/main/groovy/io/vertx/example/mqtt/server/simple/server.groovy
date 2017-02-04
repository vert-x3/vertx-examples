import io.vertx.mqtt.MqttServer

def options = [
  port:1883,
  host:"0.0.0.0"
]

def server = MqttServer.create(vertx, options)

server.endpointHandler({ endpoint ->

  println("connected client ${endpoint.clientIdentifier()}")

  endpoint.publishHandler({ message ->

    println("Just received message on [${message.topicName()}] payload [${message.payload()}] with QoS [${message.qosLevel()}]")
  })

  endpoint.accept(false)
})

server.listen({ ar ->
  if (ar.succeeded()) {
    println("MQTT server started and listening on port ${server.actualPort()}")
  } else {
    System.err.println("MQTT server error on start${ar.cause().getMessage()}")
  }
})
