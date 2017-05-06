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

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Describes a service exposed on the event bus with <a href="http://vertx.io/docs/vertx-service-proxy/java/">Vert.x Service Proxies</a>.
 *
 * @author Thomas Segismont
 */
@ProxyGen
public interface BookAsyncService {

  /**
   * The service address on the Vert.x event bus.
   */
  String ADDRESS = BookAsyncService.class.getName();

  void add(Book book, Handler<AsyncResult<Book>> resultHandler);

  void getAll(Handler<AsyncResult<List<Book>>> resultHandler);
}
