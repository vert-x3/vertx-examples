package io.vertx.example.core.execblocking;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ExecBlockingDedicatedPoolExample extends Launcher {

  public static void main(String[] args) {
    new ExecBlockingDedicatedPoolExample().dispatch(new String[]{"run", ExecBlockingExample.class.getName()});
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    deploymentOptions
      .setWorkerPoolName("dedicated-pool")
      .setMaxWorkerExecuteTime(120000)
      .setWorkerPoolSize(5);
  }
}
