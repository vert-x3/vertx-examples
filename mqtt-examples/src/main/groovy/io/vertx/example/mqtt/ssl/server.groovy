import io.vertx.mqtt.MqttServer

def options = [
  port:8883,
  pemKeyCertOptions:[
    keyPath:"server-key.pem",
    certPath:"server-cert.pem"
  ],
  ssl:true
]

def mqttServer = MqttServer.create(vertx, options)

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
