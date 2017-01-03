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

package io.vertx.example.spring.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.*;

/**
 * A worker verticle, exposing the {@link BookAsyncService} over the event bus.
 *
 * Since it is a worker verticle, it is perfectly fine for the registered service to delegate calls to backend Spring beans.
 *
 * @author Thomas Segismont
 */
@Component
// Prototype scope is needed as multiple instances of this verticle will be deployed
@Scope(SCOPE_PROTOTYPE)
public class SpringWorker extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(SpringWorker.class);

  @Autowired
  BookAsyncService bookAsyncService;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    ProxyHelper.registerService(BookAsyncService.class, vertx, bookAsyncService, BookAsyncService.ADDRESS).completionHandler(ar -> {
      if (ar.succeeded()) {
        LOG.info("SpringWorker started");
        startFuture.complete();
      } else {
        startFuture.fail(ar.cause());
      }
    });
  }

}
