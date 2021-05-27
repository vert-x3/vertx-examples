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

package io.vertx.examples.service.rxjava;

import rx.Observable;
import rx.Single;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.WriteStreamSubscriber;
import io.vertx.rx.java.SingleOnSubscribeAdapter;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import io.vertx.core.Handler;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.lang.rx.RxGen;
import io.vertx.lang.rx.TypeArg;
import io.vertx.lang.rx.MappingIterator;

/**
 * The service interface.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.examples.service.ProcessorService original} non RX-ified interface using Vert.x codegen.
 */

@RxGen(io.vertx.examples.service.ProcessorService.class)
public class ProcessorService {

  @Override
  public String toString() {
    return delegate.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProcessorService that = (ProcessorService) o;
    return delegate.equals(that.delegate);
  }
  
  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  public static final TypeArg<ProcessorService> __TYPE_ARG = new TypeArg<>(    obj -> new ProcessorService((io.vertx.examples.service.ProcessorService) obj),
    ProcessorService::getDelegate
  );

  private final io.vertx.examples.service.ProcessorService delegate;
  
  public ProcessorService(io.vertx.examples.service.ProcessorService delegate) {
    this.delegate = delegate;
  }

  public ProcessorService(Object delegate) {
    this.delegate = (io.vertx.examples.service.ProcessorService)delegate;
  }

  public io.vertx.examples.service.ProcessorService getDelegate() {
    return delegate;
  }

  public static io.vertx.examples.service.rxjava.ProcessorService create(io.vertx.rxjava.core.Vertx vertx) { 
    io.vertx.examples.service.rxjava.ProcessorService ret = io.vertx.examples.service.rxjava.ProcessorService.newInstance((io.vertx.examples.service.ProcessorService)io.vertx.examples.service.ProcessorService.create(vertx.getDelegate()));
    return ret;
  }

  public static io.vertx.examples.service.rxjava.ProcessorService createProxy(io.vertx.rxjava.core.Vertx vertx, java.lang.String address) { 
    io.vertx.examples.service.rxjava.ProcessorService ret = io.vertx.examples.service.rxjava.ProcessorService.newInstance((io.vertx.examples.service.ProcessorService)io.vertx.examples.service.ProcessorService.createProxy(vertx.getDelegate(), address));
    return ret;
  }

  public void process(io.vertx.core.json.JsonObject document, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.json.JsonObject>> resultHandler) { 
    delegate.process(document, resultHandler);
  }

  public void process(io.vertx.core.json.JsonObject document) {
    process(document, ar -> { });
  }

    public rx.Single<io.vertx.core.json.JsonObject> rxProcess(io.vertx.core.json.JsonObject document) { 
    return Single.create(new SingleOnSubscribeAdapter<>(fut -> {
      process(document, fut);
    }));
  }

  public static final int NO_NAME_ERROR = io.vertx.examples.service.ProcessorService.NO_NAME_ERROR;
  public static final int BAD_NAME_ERROR = io.vertx.examples.service.ProcessorService.BAD_NAME_ERROR;
  public static ProcessorService newInstance(io.vertx.examples.service.ProcessorService arg) {
    return arg != null ? new ProcessorService(arg) : null;
  }

}
