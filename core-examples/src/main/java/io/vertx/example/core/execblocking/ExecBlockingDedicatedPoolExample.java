package io.vertx.example.core.execblocking;

import io.vertx.launcher.application.HookContext;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.launcher.application.VertxApplicationHooks;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ExecBlockingDedicatedPoolExample extends VertxApplication implements VertxApplicationHooks {

  public ExecBlockingDedicatedPoolExample(String[] args) {
    super(args);
  }

  public static void main(String[] args) {
    new ExecBlockingDedicatedPoolExample(new String[]{ExecBlockingExample.class.getName()}).launch();
  }

  @Override
  public void beforeDeployingVerticle(HookContext context) {
    context.deploymentOptions()
      .setWorkerPoolName("dedicated-pool")
      .setMaxWorkerExecuteTime(120000)
      .setWorkerPoolSize(5);
  }
}
