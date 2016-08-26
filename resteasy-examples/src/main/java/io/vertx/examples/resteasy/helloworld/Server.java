package io.vertx.examples.resteasy.helloworld;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.resteasy.util.Runner;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // Build the Jax-RS hello world deployment
    VertxResteasyDeployment deployment = new VertxResteasyDeployment();
    deployment.start();
    deployment.getRegistry().addPerInstanceResource(HelloWorldService.class);

    // Start the front end server using the Jax-RS controller
    vertx.createHttpServer()
        .requestHandler(new VertxRequestHandler(vertx, deployment))
        .listen(8080, ar -> {
          System.out.println("Server started on port "+ ar.result().actualPort());
        });

  }
}
