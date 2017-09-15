require 'vertx-mqtt-server/mqtt_server'
require 'vertx/buffer'

mqttServer = VertxMqttServer::MqttServer.create($vertx)

mqttServer.endpoint_handler() { |endpoint|

  # shows main connect info
  puts "MQTT client [#{endpoint.client_identifier()}] request to connect, clean session = #{endpoint.clean_session?()}"

  if (endpoint.auth() != nil)
    puts "[username = #{endpoint.auth().user_name()}, password = #{endpoint.auth().password()}]"
  end
  if (endpoint.will() != nil)
    puts "[will flag = #{endpoint.will().will_flag?()} topic = #{endpoint.will().will_topic()} msg = #{endpoint.will().will_message()} QoS = #{endpoint.will().will_qos()} isRetain = #{endpoint.will().will_retain?()}]"
  end

  puts "[keep alive timeout = #{endpoint.keep_alive_time_seconds()}]"

  # accept connection from the remote client
  endpoint.accept(false)

  # handling requests for subscriptions
  endpoint.subscribe_handler() { |subscribe|

    grantedQosLevels = Array.new
    subscribe.topic_subscriptions().each do |s|
      puts "Subscription for #{s.topic_name()} with QoS #{s.quality_of_service()}"
      grantedQosLevels.push(s.quality_of_service())
    end
    # ack the subscriptions request
    endpoint.subscribe_acknowledge(subscribe.message_id(), grantedQosLevels)

    # just as example, publish a message on the first topic with requested QoS
    endpoint.publish(subscribe.topic_subscriptions()[0].topic_name(), Vertx::Buffer.buffer("Hello from the Vert.x MQTT server"), subscribe.topic_subscriptions()[0].quality_of_service(), false, false)

    # specifing handlers for handling QoS 1 and 2
    endpoint.publish_acknowledge_handler() { |messageId|

      puts "Received ack for message = #{messageId}"

    }.publish_received_handler() { |messageId|

      endpoint.publish_release(messageId)

    }.publish_completion_handler() { |messageId|

      puts "Received ack for message = #{messageId}"
    }
  }

  # handling requests for unsubscriptions
  endpoint.unsubscribe_handler() { |unsubscribe|

    unsubscribe.topics().each do |t|
      puts "Unsubscription for #{t}"
    end
    # ack the subscriptions request
    endpoint.unsubscribe_acknowledge(unsubscribe.message_id())
  }

  # handling ping from client
  endpoint.ping_handler() { |v|

    puts "Ping received from client"
  }

  # handling disconnect message
  endpoint.disconnect_handler() { |v|

    puts "Received disconnect from client"
  }

  # handling closing connection
  endpoint.close_handler() { |v|

    puts "Connection closed"
  }

  # handling incoming published messages
  endpoint.publish_handler() { |message|

    puts "Just received message on [#{message.topic_name()}] payload [#{message.payload()}] with QoS [#{message.qos_level()}]"

    if (message.qos_level() == :AT_LEAST_ONCE)
      endpoint.publish_acknowledge(message.message_id())
    elsif (message.qos_level() == :EXACTLY_ONCE)
      endpoint.publish_received(message.message_id())
    end

  }.publish_release_handler() { |messageId|
    endpoint.publish_complete(messageId)
  }
}.listen(1883, "0.0.0.0") { |ar_err,ar|

  if (ar_err == nil)
    puts "MQTT server is listening on port #{mqttServer.actual_port()}"
  else
    STDERR.puts "Error on starting the server#{ar_err.get_message()}"
  end
}
