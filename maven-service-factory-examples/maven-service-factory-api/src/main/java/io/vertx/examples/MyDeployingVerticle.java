package io.vertx.examples;

import io.vertx.core.AbstractVerticle;

/**
 * This verticle uses the Maven Service Factory to deploy `my-verticle`.
 */
public class MyDeployingVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    // The `my-verticle` is deployed using the follwoing convention:
    // `maven:` + groupId + `:` + artifactId + `:` + version + `::` + verticle name
    vertx.deployVerticle("maven:io.vertx:maven-service-factory-verticle:3.0.0-SNAPSHOT::my-verticle");
  }
}
