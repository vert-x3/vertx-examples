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

package io.vertx.example.mqtt.server.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.mqtt.server.util.Runner;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

/**
 * An example of using the MQTT server as a verticle
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    MqttServerOptions options = new MqttServerOptions()
      .setPort(1883)
      .setHost("0.0.0.0");

    MqttServer server = MqttServer.create(vertx, options);

    server.endpointHandler(endpoint -> {

      System.out.println("connected client " + endpoint.clientIdentifier());

      endpoint.publishHandler(message -> {

        System.out.println("Just received message on [" + message.topicName() + "] payload [" +
          message.payload() + "] with QoS [" +
          message.qosLevel() + "]");
      });

      endpoint.accept(false);
    });

    server.listen(ar -> {
      if (ar.succeeded()) {
        System.out.println("MQTT server started and listening on port " + server.actualPort());
      } else {
        System.err.println("MQTT server error on start" + ar.cause().getMessage());
      }
    });
  }
}

