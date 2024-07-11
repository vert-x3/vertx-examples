package io.vertx.example.core.verticle.asyncstart;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

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

    // Deploy another instance and  want for it to start
    vertx.deployVerticle("io.vertx.example.core.verticle.asyncstart.OtherVerticle")
      .onComplete(res -> {
      if (res.succeeded()) {

        String deploymentID = res.result();

        System.out.println("Other verticle deployed ok, deploymentID = " + deploymentID);

        vertx.undeploy(deploymentID)
          .onComplete(res2 -> {
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


  }
}
