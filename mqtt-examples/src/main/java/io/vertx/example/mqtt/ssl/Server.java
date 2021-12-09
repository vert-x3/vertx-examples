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

package io.vertx.example.mqtt.ssl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.example.mqtt.util.Runner;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

/**
 * An example of using the MQTT server with TLS support
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    MqttServerOptions options = new MqttServerOptions()
      .setPort(8883)
      .setPemKeyCertOptions(new PemKeyCertOptions()
        .setKeyPath("server-key.pem")
        .setCertPath("server-cert.pem"))
      .setSsl(true);

    MqttServer mqttServer = MqttServer.create(vertx, options);

    mqttServer
      .endpointHandler(endpoint -> {

        // shows main connect info
        System.out.println("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, " +
          "clean session = " + endpoint.isCleanSession());

        // accept connection from the remote client
        endpoint.accept(false);

      })
      .listen(ar -> {

        if (ar.succeeded()) {
          System.out.println("MQTT server is listening on port " + mqttServer.actualPort());
        } else {
          System.err.println("Error on starting the server" + ar.cause().getMessage());
        }
      });
  }
}
