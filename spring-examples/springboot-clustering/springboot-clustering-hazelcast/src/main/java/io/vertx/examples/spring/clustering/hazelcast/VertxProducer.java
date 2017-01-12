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

import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A component managing the lifecycle of the clustered Vert.x instance.
 * It uses the Spring-managed {@link HazelcastInstance}.
 *
 * This bean is created <strong>after</strong> and destroyed <strong>before</strong> the {@link HazelcastInstance}.
 *
 * @author Thomas Segismont
 */
@Component
public class VertxProducer {

  @Autowired
  HazelcastInstance hazelcastInstance;

  private Vertx vertx;

  @PostConstruct
  void init() throws ExecutionException, InterruptedException {
    VertxOptions options = new VertxOptions()
      .setClusterManager(new HazelcastClusterManager(hazelcastInstance));
    CompletableFuture<Vertx> future = new CompletableFuture<>();
    Vertx.clusteredVertx(options, ar -> {
      if (ar.succeeded()) {
        future.complete(ar.result());
      } else {
        future.completeExceptionally(ar.cause());
      }
    });
    vertx = future.get();
  }

  /**
   * Exposes the clustered Vert.x instance.
   * We must disable destroy method inference, otherwise Spring will call the {@link Vertx#close()} automatically.
   */
  @Bean(destroyMethod = "")
  Vertx vertx() {
    return vertx;
  }

  @PreDestroy
  void close() throws ExecutionException, InterruptedException {
    CompletableFuture<Void> future = new CompletableFuture<>();
    vertx.close(ar -> future.complete(null));
    future.get();
  }
}
