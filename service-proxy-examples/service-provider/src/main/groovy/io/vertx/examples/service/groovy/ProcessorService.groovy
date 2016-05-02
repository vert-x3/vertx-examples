/*
 * Copyright 2014 Red Hat, Inc.
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

package io.vertx.examples.service.groovy;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * The service interface.
*/
@CompileStatic
public class ProcessorService {
  private final def io.vertx.examples.service.ProcessorService delegate;
  public ProcessorService(Object delegate) {
    this.delegate = (io.vertx.examples.service.ProcessorService) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static ProcessorService create(Vertx vertx) {
    def ret = InternalHelper.safeCreate(io.vertx.examples.service.ProcessorService.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), io.vertx.examples.service.groovy.ProcessorService.class);
    return ret;
  }
  public static ProcessorService createProxy(Vertx vertx, String address) {
    def ret = InternalHelper.safeCreate(io.vertx.examples.service.ProcessorService.createProxy(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, address), io.vertx.examples.service.groovy.ProcessorService.class);
    return ret;
  }
  public void process(Map<String, Object> document, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.process(document != null ? new io.vertx.core.json.JsonObject(document) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.core.json.JsonObject>>() {
      public void handle(AsyncResult<io.vertx.core.json.JsonObject> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
}
