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

package io.vertx.rxjava.examples.service;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * The service interface.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.examples.service.ProcessorService original} non RX-ified interface using Vert.x codegen.
 */

public class ProcessorService {

  final io.vertx.examples.service.ProcessorService delegate;

  public ProcessorService(io.vertx.examples.service.ProcessorService delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static ProcessorService create(Vertx vertx) { 
    ProcessorService ret= ProcessorService.newInstance(io.vertx.examples.service.ProcessorService.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public static ProcessorService createProxy(Vertx vertx, String address) { 
    ProcessorService ret= ProcessorService.newInstance(io.vertx.examples.service.ProcessorService.createProxy((io.vertx.core.Vertx) vertx.getDelegate(), address));
    return ret;
  }

  public void process(JsonObject document, Handler<AsyncResult<JsonObject>> resultHandler) { 
    this.delegate.process(document, resultHandler);
  }

  public Observable<JsonObject> processObservable(JsonObject document) { 
    io.vertx.rx.java.ObservableFuture<JsonObject> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    process(document, resultHandler.toHandler());
    return resultHandler;
  }


  public static ProcessorService newInstance(io.vertx.examples.service.ProcessorService arg) {
    return arg != null ? new ProcessorService(arg) : null;
  }
}
