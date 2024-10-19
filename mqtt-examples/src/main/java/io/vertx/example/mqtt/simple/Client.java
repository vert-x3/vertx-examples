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

package io.vertx.example.mqtt.simple;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.mqtt.MqttClient;

/**
 * An example of using the MQTT client as a verticle
 */
public class Client extends VerticleBase {

  private static final String MQTT_TOPIC = "/my_topic";
  private static final String MQTT_MESSAGE = "Hello Vert.x MQTT Client";
  private static final String BROKER_HOST = "localhost";
  private static final int BROKER_PORT = 1883;

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {
    MqttClient mqttClient = MqttClient.create(vertx);

    return mqttClient.connect(BROKER_PORT, BROKER_HOST)
      .compose(ch -> {
      System.out.println("Connected to a server");
      return mqttClient.publish(
          MQTT_TOPIC,
          Buffer.buffer(MQTT_MESSAGE),
          MqttQoS.AT_MOST_ONCE,
          false,
          false)
        .eventually(mqttClient::disconnect)
        .onSuccess(d -> System.out.println("Disconnected from server"));
    });
  }
}
