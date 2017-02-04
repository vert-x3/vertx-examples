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

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.IOException;

/**
 * An example of starting the MQTT server as verticle
 */
public class MqttServerVerticleApp {

  private static final Logger log = LoggerFactory.getLogger(MqttServerVerticleApp.class);

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();

    DeploymentOptions options = new DeploymentOptions();
    options.setInstances(5);

    vertx.deployVerticle("io.vertx.example.mqtt.MqttServerVerticle", options);

    try {
      System.in.read();
      vertx.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
