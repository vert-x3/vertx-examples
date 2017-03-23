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
import io.vertx.core.http.HttpServerOptions;

/**
 * @author Thomas Segismont
 */
public class HttpVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    HttpServerOptions options = new HttpServerOptions().setPort(config().getInteger("port"));
    vertx.createHttpServer(options).requestHandler(request -> {
      String name = request.getParam("name");
      if (name == null) {
        request.response().setStatusCode(400).end("Missing name");
      } else {
        vertx.eventBus().<String>send("hello", name, ar -> {
          if (ar.succeeded()) {
            request.response().end(ar.result().body());
          } else {
            request.response().setStatusCode(500).end(ar.cause().getMessage());
          }
        });
      }
    }).listen(ar -> {
      if (ar.succeeded()) {
        startFuture.complete();
      } else {
        startFuture.fail(ar.cause());
      }
    });
  }
}
