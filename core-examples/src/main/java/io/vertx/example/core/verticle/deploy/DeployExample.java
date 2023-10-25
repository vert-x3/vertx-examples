package io.vertx.example.core.verticle.deploy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.ThreadingModel;
import io.vertx.core.json.JsonObject;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class DeployExample extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", DeployExample.class.getName());
  }

  @Override
  public void start() throws Exception {

    System.out.println("Main verticle has started, let's deploy some others...");

    // Different ways of deploying verticles

    // Deploy a verticle and don't wait for it to start
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle");

    // Deploy another instance and  want for it to start
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", res -> {
      if (res.succeeded()) {

        String deploymentID = res.result();

        System.out.println("Other verticle deployed ok, deploymentID = " + deploymentID);

        // You can also explicitly undeploy a verticle deployment.
        // Note that this is usually unnecessary as any verticles deployed by a verticle will be automatically
        // undeployed when the parent verticle is undeployed

        vertx.undeploy(deploymentID, res2 -> {
          if (res2.succeeded()) {
            System.out.println("Undeployed ok!");
          } else {
            res2.cause().printStackTrace();
          }
        });

      } else {
        res.cause().printStackTrace();
      }
    });

    // Deploy specifying some config
    JsonObject config = new JsonObject().put("foo", "bar");
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", new DeploymentOptions().setConfig(config));

    // Deploy 10 instances
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", new DeploymentOptions().setInstances(10));

    // Deploy it as a worker verticle
    vertx.deployVerticle("io.vertx.example.core.verticle.deploy.OtherVerticle", new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER));


  }
}
