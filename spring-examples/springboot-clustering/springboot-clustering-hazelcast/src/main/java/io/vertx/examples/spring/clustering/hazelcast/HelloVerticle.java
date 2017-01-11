/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.examples.spring.clustering.hazelcast;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import java.util.UUID;

/**
 * @author Thomas Segismont
 */
public class HelloVerticle extends AbstractVerticle {
  private static final String ID = UUID.randomUUID().toString();

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx.eventBus().<String>consumer("hello", message -> {
      message.reply("Hello " + message.body() + " from " + ID);
    }).completionHandler(startFuture.completer());
  }
}
