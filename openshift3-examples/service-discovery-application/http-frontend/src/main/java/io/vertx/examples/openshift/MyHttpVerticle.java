package io.vertx.examples.openshift;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.kubernetes.KubernetesServiceImporter;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class MyHttpVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    // Create the service discovery object and register the Kubernetes importer
    // We limit the lookup to the current namespace.
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new KubernetesServiceImporter(), new JsonObject().put("namespace", "vertx-demo"));

    Router router = Router.router(vertx);
    router.get("/").handler(rc -> {
      String param = rc.request().getParam("name") == null ? "world" : rc.request().getParam("name");
      HttpEndpoint.getClient(discovery, new JsonObject().put("name", "http-backend"), ar -> {
        if (ar.failed()) {
          rc.response().setStatusCode(400).end("No `http-backend` service");
        } else {
          ar.result().getNow("/?name=" + param, request -> {
            request.bodyHandler(body -> {
              rc.response().end(body);
              ar.result().close();
            });
          });
        }
      });
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);
  }
}
