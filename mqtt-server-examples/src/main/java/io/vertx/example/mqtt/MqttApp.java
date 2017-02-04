/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.example.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttTopicSubscription;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * An example of using the MQTT server
 */
public class MqttApp {

  private static final Logger log = LoggerFactory.getLogger(MqttApp.class);

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();

    MqttServer mqttServer = MqttServer.create(vertx);

    mqttServer
      .endpointHandler(endpoint -> {

        // shows main connect info
        log.info("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());

        if (endpoint.auth() != null) {
          log.info("[username = " + endpoint.auth().userName() + ", password = " + endpoint.auth().password() + "]");
        }
        if (endpoint.will() != null) {
          log.info("[will flag = " + endpoint.will().isWillFlag() + " topic = " + endpoint.will().willTopic() + " msg = " + endpoint.will().willMessage() +
            " QoS = " + endpoint.will().willQos() + " isRetain = " + endpoint.will().isWillRetain() + "]");
        }

        log.info("[keep alive timeout = " + endpoint.keepAliveTimeSeconds() + "]");

        // accept connection from the remote client
        endpoint.accept(false);

        // handling requests for subscriptions
        endpoint.subscribeHandler(subscribe -> {

          List<Integer> grantedQosLevels = new ArrayList<>();
          for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
            log.info("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
            grantedQosLevels.add(s.qualityOfService().value());
          }
          // ack the subscriptions request
          endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

          // just as example, publish a message on the first topic with requested QoS
          endpoint.publish(subscribe.topicSubscriptions().get(0).topicName(),
            Buffer.buffer("Hello from the Vert.x MQTT server"),
            subscribe.topicSubscriptions().get(0).qualityOfService(),
            false,
            false);

          // specifing handlers for handling QoS 1 and 2
          endpoint.publishAcknowledgeHandler(messageId -> {

            log.info("Received ack for message = " + messageId);

          }).publishReceivedHandler(messageId -> {

            endpoint.publishRelease(messageId);

          }).publishCompleteHandler(messageId -> {

            log.info("Received ack for message = " + messageId);
          });
        });

        // handling requests for unsubscriptions
        endpoint.unsubscribeHandler(unsubscribe -> {

          for (String t : unsubscribe.topics()) {
            log.info("Unsubscription for " + t);
          }
          // ack the subscriptions request
          endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
        });

        // handling ping from client
        endpoint.pingHandler(v -> {

          log.info("Ping received from client");
        });

        // handling disconnect message
        endpoint.disconnectHandler(v -> {

          log.info("Received disconnect from client");
        });

        // handling closing connection
        endpoint.closeHandler(v -> {

          log.info("Connection closed");
        });

        // handling incoming published messages
        endpoint.publishHandler(message -> {

          log.info("Just received message on [" + message.topicName() + "] payload [" + message.payload().toString(Charset.defaultCharset()) + "] with QoS [" + message.qosLevel() + "]");

          if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
            endpoint.publishAcknowledge(message.messageId());
          } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
            endpoint.publishReceived(message.messageId());
          }

        }).publishReleaseHandler(messageId -> {

          endpoint.publishComplete(messageId);
        });

      })
      .listen(ar -> {

        if (ar.succeeded()) {
          log.info("MQTT server is listening on port " + ar.result().actualPort());
        } else {
          log.error("Error on starting the server", ar.cause());
        }
      });

    try {
      System.in.read();
      mqttServer.close(v -> {
        log.info("MQTT server closed");
      });
      vertx.close();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }
}
