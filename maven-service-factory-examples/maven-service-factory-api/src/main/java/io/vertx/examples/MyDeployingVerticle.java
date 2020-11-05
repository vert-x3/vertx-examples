package io.vertx.examples;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

/**
 * This verticle uses the Maven Service Factory to deploy `my-verticle`.
 */
public class MyDeployingVerticle extends AbstractVerticle {


  @Override
  public void start(Promise<Void> promise) {
    // The `my-verticle` is deployed using the following convention:
    // `maven:` + groupId + `:` + artifactId + `:` + version + `::` + verticle name
    vertx.deployVerticle("maven:io.vertx:maven-service-factory-verticle:4.0.0.CR1::my-verticle", ar -> {
      if (ar.succeeded()) {
        promise.complete();
      } else {
        promise.fail(ar.cause());
      }
    });
  }
}
