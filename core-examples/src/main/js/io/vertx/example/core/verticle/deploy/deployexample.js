
console.log("Main verticle has started, let's deploy some others...");

// Different ways of deploying verticles

// Deploy a verticle and don't wait for it to start
vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle");

// Deploy another instance and  want for it to start
vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", function (res, res_err) {
  if (res_err == null) {

    var deploymentID = res;

    console.log("Other verticle deployed ok, deploymentID = " + deploymentID);

    // You can also explicitly undeploy a verticle deployment.
    // Note that this is usually unnecessary as any verticles deployed by a verticle will be automatically
    // undeployed when the parent verticle is undeployed

    vertx.undeploy(deploymentID, function (res2, res2_err) {
      if (res2_err == null) {
        console.log("Undeployed ok!");
      } else {
        res2_err.printStackTrace();
      };
    });

  } else {
    res_err.printStackTrace();
  };
});

// Deploy specifying some config
var config = {
  "foo" : "bar"
};
vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", {
  "config" : config
});

// Deploy 10 instances
vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", {
  "instances" : 10
});

// Deploy it as a worker verticle
vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", {
  "worker" : true
});


