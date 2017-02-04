package io.vertx.example.core.verticle.deploy

import io.vertx.kotlin.common.json.*

class DeployExample : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    println("Main verticle has started, let's deploy some others...")

    // Different ways of deploying verticles

    // Deploy a verticle and don't wait for it to start
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle")

    // Deploy another instance and  want for it to start
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", { res ->
      if (res.succeeded()) {

        var deploymentID = res.result()

        println("Other verticle deployed ok, deploymentID = ${deploymentID}")

        // You can also explicitly undeploy a verticle deployment.
        // Note that this is usually unnecessary as any verticles deployed by a verticle will be automatically
        // undeployed when the parent verticle is undeployed

        vertx.undeploy(deploymentID, { res2 ->
          if (res2.succeeded()) {
            println("Undeployed ok!")
          } else {
            res2.cause().printStackTrace()
          }
        })

      } else {
        res.cause().printStackTrace()
      }
    })

    // Deploy specifying some config
    var config = json {
      obj("foo" to "bar")
    }
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", io.vertx.core.DeploymentOptions(
      config = config))

    // Deploy 10 instances
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", io.vertx.core.DeploymentOptions(
      instances = 10))

    // Deploy it as a worker verticle
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", io.vertx.core.DeploymentOptions(
      worker = true))


  }
}
