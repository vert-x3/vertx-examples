package io.vertx.examples;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * This verticle uses the Maven Service Factory to deploy `my-verticle`.
 */
public class MyDeployingVerticle extends AbstractVerticle {


  @Override
  public void start(Future<Void> future) throws Exception {
    // The `my-verticle` is deployed using the following convention:
    // `maven:` + groupId + `:` + artifactId + `:` + version + `::` + verticle name
    vertx.deployVerticle("maven:io.vertx:maven-service-factory-verticle:3.4.0-SNAPSHOT::my-verticle",
        ar -> {
          if (ar.succeeded()) {
            future.complete();
          } else {
            future.fail(ar.cause());
          }
        }
    );
  }
}
