require 'vertx/vertx'
require 'vertx-mqtt-server/mqtt_client'
require 'vertx/buffer'
@MQTT_MESSAGE = "Hello Vert.x MQTT Client"
@BROKER_HOST = "localhost"
@MQTT_TOPIC = "/my_topic"
@BROKER_PORT = 1883
options = {
  'keepAliveTimeSeconds' => 2
}

client = VertxMqttServer::MqttClient.create(Vertx::Vertx.vertx(), options)


# handler will be called when we have a message in topic we subscribing for
client.publish_handler() { |publish|
  puts "Just received message on [#{publish.topic_name()}] payload [#{publish.payload().to_string(Java::JavaNioCharset::Charset.default_charset())}] with QoS [#{publish.qos_level()}]"
}

# handle response on subscribe request
client.subscribe_completion_handler() { |h|
  puts "Receive SUBACK from server with granted QoS : #{h.granted_qo_s_levels()}"

  # let's publish a message to the subscribed topic
  client.publish(@MQTT_TOPIC, Vertx::Buffer.buffer(@MQTT_MESSAGE), :AT_MOST_ONCE, false, false) { |s_err,s|
    puts "Publish sent to a server"
  }

  # unsubscribe from receiving messages for earlier subscribed topic
  $vertx.set_timer(5000) { |l|
    client.unsubscribe(@MQTT_TOPIC)
  }
}

# handle response on unsubscribe request
client.unsubscribe_completion_handler() { |h|
  puts "Receive UNSUBACK from server"
  $vertx.set_timer(5000) { |l|
    client.disconnect() { |d_err,d|
      puts "Disconnected form server"
    }
  }
}

# connect to a server
client.connect(@BROKER_PORT, @BROKER_HOST) { |ch_err,ch|
  if (ch_err == nil)
    puts "Connected to a server"
    client.subscribe(@MQTT_TOPIC, 0)
  else
    puts "Failed to connect to a server"
    puts ch_err
  end
}
