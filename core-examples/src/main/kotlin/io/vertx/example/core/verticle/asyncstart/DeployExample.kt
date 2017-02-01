import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    println("Main verticle has started, let's deploy some others...")

    // Deploy another instance and  want for it to start
    vertx.deployVerticle("io.vertx.example.core.verticle.asyncstart.OtherVerticle", { res ->
      if (res.succeeded()) {

        var deploymentID = res.result()

        println("Other verticle deployed ok, deploymentID = ${deploymentID}")

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


  }
}
