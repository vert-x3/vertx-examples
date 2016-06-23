package io.vertx.example.core.execblocking;

import io.vertx.core.DeploymentOptions;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ExecBlockingDedicatedPoolExample {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ExecBlockingExample.class, new DeploymentOptions()
        .setWorkerPoolName("dedicated-pool")
        .setMaxWorkerExecuteTime(120000)
        .setWorkerPoolSize(5));
  }

}
