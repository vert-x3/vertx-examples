package io.vertx.example.mqtt.app

import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.mqtt.MqttServer

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var mqttServer = MqttServer.create(vertx)

    mqttServer.endpointHandler({ endpoint ->

      // shows main connect info
      println("MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession()}")

      if (endpoint.auth() != null) {
        println("[username = ${endpoint.auth().userName()}, password = ${endpoint.auth().password()}]")
      }
      if (endpoint.will() != null) {
        println("[will flag = ${endpoint.will().isWillFlag()} topic = ${endpoint.will().willTopic()} msg = ${endpoint.will().willMessage()} QoS = ${endpoint.will().willQos()} isRetain = ${endpoint.will().isWillRetain()}]")
      }

      println("[keep alive timeout = ${endpoint.keepAliveTimeSeconds()}]")

      // accept connection from the remote client
      endpoint.accept(false)

      // handling requests for subscriptions
      endpoint.subscribeHandler({ subscribe ->

        var grantedQosLevels = mutableListOf<Any?>()
        for (s in subscribe.topicSubscriptions()) {
          println("Subscription for ${s.topicName()} with QoS ${s.qualityOfService()}")
          grantedQosLevels.add(s.qualityOfService())
        }
        // ack the subscriptions request
        endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels)

        // just as example, publish a message on the first topic with requested QoS
        endpoint.publish(subscribe.topicSubscriptions()[0].topicName(), Buffer.buffer("Hello from the Vert.x MQTT server"), subscribe.topicSubscriptions()[0].qualityOfService(), false, false)

        // specifing handlers for handling QoS 1 and 2
        endpoint.publishAcknowledgeHandler({ messageId ->

          println("Received ack for message = ${messageId}")

        }).publishReceivedHandler({ messageId ->

          endpoint.publishRelease(messageId)

        }).publishCompleteHandler({ messageId ->

          println("Received ack for message = ${messageId}")
        })
      })

      // handling requests for unsubscriptions
      endpoint.unsubscribeHandler({ unsubscribe ->

        for (t in unsubscribe.topics()) {
          println("Unsubscription for ${t}")
        }
        // ack the subscriptions request
        endpoint.unsubscribeAcknowledge(unsubscribe.messageId())
      })

      // handling ping from client
      endpoint.pingHandler({ v ->

        println("Ping received from client")
      })

      // handling disconnect message
      endpoint.disconnectHandler({ v ->

        println("Received disconnect from client")
      })

      // handling closing connection
      endpoint.closeHandler({ v ->

        println("Connection closed")
      })

      // handling incoming published messages
      endpoint.publishHandler({ message ->

        println("Just received message on [${message.topicName()}] payload [${message.payload()}] with QoS [${message.qosLevel()}]")

        if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
          endpoint.publishAcknowledge(message.messageId())
        } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
          endpoint.publishReceived(message.messageId())
        }

      }).publishReleaseHandler({ messageId ->
        endpoint.publishComplete(messageId)
      })
    }).listen(1883, "0.0.0.0", { ar ->

      if (ar.succeeded()) {
        println("MQTT server is listening on port ${mqttServer.actualPort()}")
      } else {
        System.err.println("Error on starting the server${ar.cause().getMessage()}")
      }
    })
  }
}
