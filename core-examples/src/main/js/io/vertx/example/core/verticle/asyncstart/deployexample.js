
console.log("Main verticle has started, let's deploy some others...");

// Deploy another instance and  want for it to start
vertx.deployVerticle("io.vertx.example.core.verticle.asyncstart.OtherVerticle", function (res, res_err) {
  if (res_err == null) {

    var deploymentID = res;

    console.log("Other verticle deployed ok, deploymentID = " + deploymentID);

    vertx.undeploy(deploymentID, function (res2, res2_err) {
      if (res2_err == null) {
        console.log("Undeployed ok!");
      } else {
        res2_err.printStackTrace();
      }
    });
  } else {
    res_err.printStackTrace();
  }
});


