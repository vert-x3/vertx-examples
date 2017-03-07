package io.vertx.examples.consul;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.ext.consul.ConsulClient;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientVerticle extends AbstractVerticle {

  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    Launcher.main(new String[]{"run", ConsulClientVerticle.class.getName()});
  }


  @Override
  public void start() throws Exception {
    ConsulClient consulClient = ConsulClient.create(vertx);
    consulClient.putValue("key11", "value11", putResult -> {
      if (putResult.succeeded()) {
        System.out.println("KV pair saved");
        consulClient.getValue("key11", getResult -> {
          if (getResult.succeeded()) {
            System.out.println("KV pair retrieved");
            System.out.println(getResult.result().getValue());
          } else {
            getResult.cause().printStackTrace();
          }
        });
      } else {
        putResult.cause().printStackTrace();
      }
    });
  }
}
