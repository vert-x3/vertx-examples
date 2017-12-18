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

package io.vertx.example.reactivex.services.serviceproxy.reactivex;

import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


@io.vertx.lang.reactivex.RxGen(io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService.class)
public class SomeDatabaseService {

  @Override
  public String toString() {
    return delegate.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SomeDatabaseService that = (SomeDatabaseService) o;
    return delegate.equals(that.delegate);
  }
  
  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  public static final io.vertx.lang.reactivex.TypeArg<SomeDatabaseService> __TYPE_ARG = new io.vertx.lang.reactivex.TypeArg<>(
    obj -> new SomeDatabaseService((io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService) obj),
    SomeDatabaseService::getDelegate
  );

  private final io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService delegate;
  
  public SomeDatabaseService(io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService delegate) {
    this.delegate = delegate;
  }

  public io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService getDelegate() {
    return delegate;
  }

  public SomeDatabaseService getDataById(int id, Handler<AsyncResult<JsonObject>> resultHandler) { 
    delegate.getDataById(id, resultHandler);
    return this;
  }

  public Single<JsonObject> rxGetDataById(int id) { 
    return new io.vertx.reactivex.core.impl.AsyncResultSingle<JsonObject>(handler -> {
      getDataById(id, handler);
    });
  }


  public static  SomeDatabaseService newInstance(io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService arg) {
    return arg != null ? new SomeDatabaseService(arg) : null;
  }
}
