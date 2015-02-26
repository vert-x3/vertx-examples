package io.vertx.example.core.verticle.asyncstart;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class DeployExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(DeployExample.class);
  }

  @CodeTranslate
  @Override
  public void start() throws Exception {

    System.out.println("Main verticle has started, let's deploy some others...");

    // Deploy another instance and  want for it to start
    vertx.deployVerticle(OtherVerticle.class.getName(), res -> {
      if (res.succeeded()) {

        String deploymentID = res.result();

        System.out.println("Other verticle deployed ok, deploymentID = " + deploymentID);

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


  }
}
