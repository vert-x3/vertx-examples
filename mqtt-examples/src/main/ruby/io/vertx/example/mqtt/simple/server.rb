require 'vertx-mqtt/mqtt_server'

options = {
  'port' => 1883,
  'host' => "0.0.0.0"
}

server = VertxMqtt::MqttServer.create($vertx, options)

server.endpoint_handler() { |endpoint|

  puts "connected client #{endpoint.client_identifier()}"

  endpoint.publish_handler() { |message|

    puts "Just received message on [#{message.topic_name()}] payload [#{message.payload()}] with QoS [#{message.qos_level()}]"
  }

  endpoint.accept(false)
}

server.listen() { |ar_err,ar|
  if (ar_err == nil)
    puts "MQTT server started and listening on port #{server.actual_port()}"
  else
    STDERR.puts "MQTT server error on start#{ar_err.get_message()}"
  end
}
