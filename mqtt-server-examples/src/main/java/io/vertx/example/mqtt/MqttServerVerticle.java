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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

import java.nio.charset.Charset;

/**
 * An example of using the MQTT server as a verticle
 */
public class MqttServerVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MqttServerVerticle.class);

    private MqttServer server;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        MqttServerOptions options = new MqttServerOptions();
        options.setHost("0.0.0.0").setPort(1883);

        this.server = MqttServer.create(this.vertx, options);

        this.server.endpointHandler(endpoint -> {

            log.info("connected client " + endpoint.clientIdentifier() + " on verticle id = " + this.deploymentID());

            endpoint.publishHandler(message -> {

                log.info("Just received message on [" + message.topicName() + "] payload [" + message.payload().toString(Charset.defaultCharset()) + "] with QoS [" + message.qosLevel() + "]");

            });

            endpoint.accept(false);
        });

        this.server.listen(ar -> {

            if (ar.succeeded()) {
                log.info("MQTT server started and listening on port " + ar.result().actualPort());
            } else {
                log.error("MQTT server error on start", ar.cause());
            }

        });

    }
}

