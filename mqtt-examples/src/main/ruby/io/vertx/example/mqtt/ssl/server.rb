require 'vertx-mqtt/mqtt_server'

options = {
  'port' => 8883,
  'pemKeyCertOptions' => {
    'keyPath' => "server-key.pem",
    'certPath' => "server-cert.pem"
  },
  'ssl' => true
}

mqttServer = VertxMqtt::MqttServer.create($vertx, options)

mqttServer.endpoint_handler() { |endpoint|

  # shows main connect info
  puts "MQTT client [#{endpoint.client_identifier()}] request to connect, clean session = #{endpoint.clean_session?()}"

  # accept connection from the remote client
  endpoint.accept(false)

}.listen() { |ar_err,ar|

  if (ar_err == nil)
    puts "MQTT server is listening on port #{mqttServer.actual_port()}"
  else
    STDERR.puts "Error on starting the server#{ar_err.get_message()}"
  end
}
