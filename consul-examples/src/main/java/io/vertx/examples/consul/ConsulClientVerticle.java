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
  public void start() {
    ConsulClient consulClient = ConsulClient.create(vertx);
    consulClient.putValue("key11", "value11")
      .compose(v -> {
        System.out.println("KV pair saved");
        return consulClient.getValue("key11");
      }).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("KV pair retrieved");
        System.out.println(ar.result().getValue());
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}
