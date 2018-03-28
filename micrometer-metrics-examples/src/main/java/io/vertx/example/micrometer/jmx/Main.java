/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertx.example.micrometer.jmx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.example.micrometer.verticles.EventbusConsumer;
import io.vertx.example.micrometer.verticles.EventbusProducer;
import io.vertx.example.micrometer.verticles.SimpleWebServer;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxInfluxDbOptions;
import io.vertx.micrometer.VertxJmxMetricsOptions;

/**
 * @author Joel Takvorian, jtakvori@redhat.com
 */
public final class Main {
  public static void main(String[] args) {
    // Default JMX options will publish MBeans under domain "metrics"
    MicrometerMetricsOptions options = new MicrometerMetricsOptions()
      .setJmxMetricsOptions(new VertxJmxMetricsOptions().setEnabled(true))
      .setEnabled(true);
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(options));
    vertx.deployVerticle(new SimpleWebServer());
    vertx.deployVerticle(new EventbusConsumer());
    vertx.deployVerticle(new EventbusProducer());
  }
}
