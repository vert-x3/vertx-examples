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

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

import java.io.IOException;

/**
 * An example of using the MQTT server with TLS support
 */
public class MqttSslApp {

  private static final Logger log = LoggerFactory.getLogger(MqttSslApp.class);

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();

    PemKeyCertOptions pemKeyCertOptions = new PemKeyCertOptions()
      .setKeyPath("./src/test/resources/tls/server-key.pem")
      .setCertPath("./src/test/resources/tls/server-cert.pem");

    MqttServerOptions options = new MqttServerOptions()
      .setPort(MqttServerOptions.DEFAULT_TLS_PORT)
      .setKeyCertOptions(pemKeyCertOptions)
      .setSsl(true);

    MqttServer mqttServer = MqttServer.create(vertx, options);

    mqttServer
      .endpointHandler(endpoint -> {

        // shows main connect info
        log.info("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());

        // accept connection from the remote client
        endpoint.accept(false);

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
