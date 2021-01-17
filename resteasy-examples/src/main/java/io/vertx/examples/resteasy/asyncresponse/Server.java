package io.vertx.examples.resteasy.asyncresponse;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.vertx.core.AbstractVerticle;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.jboss.resteasy.plugins.server.vertx.VertxRegistry;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    /**
     * Cors
     * https://docs.jboss.org/resteasy/docs/4.5.8.Final/userguide/html_single/index.html#d4e2124
     */
    CorsFilter filter = new CorsFilter();
    filter.getAllowedOrigins().add("*");
    filter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
    filter.setAllowedHeaders("Content-Type, Authorization");
    filter.setCorsMaxAge(86400);//Max in FF 86400=24h https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Max-Age

    /**
     * https://docs.jboss.org/resteasy/docs/1.1.GA/userguide/html/Installation_Configuration.html#javax.ws.rs.core.Application
     */
    ApplicationRs application = new ApplicationRs();
    application.getSingletons().add(filter);

    // Build the Jax-RS controller deployment
    VertxResteasyDeployment deployment = new VertxResteasyDeployment();
    deployment.setApplication(application);
    deployment.start();
    VertxRegistry registry = deployment.getRegistry();
    registry.addPerInstanceResource(Controller.class);
    registry.addPerInstanceResource(OpenApiResource.class);


    // Start the front end server using the Jax-RS controller
    vertx.createHttpServer()
      .requestHandler(new VertxRequestHandler(vertx, deployment))
      .listen(8080, ar -> {
        if (ar.succeeded()) {
          System.out.println("Server started on port " + ar.result().actualPort());
        } else {
          ar.cause().printStackTrace();
        }
      });
  }
}
