package io.vertx.example.web.openapi3;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.RequestParameter;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.api.validation.ValidationException;


public class OpenAPI3Server extends AbstractVerticle {

  HttpServer server;
  Logger logger = LoggerFactory.getLogger("OpenAPI3RouterFactory");

  public void start(Future future) {
    // Load the api spec. This operation is asynchronous
    OpenAPI3RouterFactory.create(this.vertx, "petstore.yaml", openAPI3RouterFactoryAsyncResult -> {
      if (openAPI3RouterFactoryAsyncResult.failed()) {
        // Something went wrong during router factory initialization
        Throwable exception = openAPI3RouterFactoryAsyncResult.cause();
        logger.error("oops, something went wrong during factory initialization", exception);
        future.fail(exception);
      }
      // Spec loaded with success
      OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();
      // Add an handler with operationId
      routerFactory.addHandlerByOperationId("listPets", routingContext -> {
        // Load the parsed parameters
        RequestParameters params = routingContext.get("parsedParameters");
        // Handle listPets operation
        RequestParameter limitParameter = params.queryParameter(/* Parameter name */ "limit");
        if (limitParameter != null) {
          // limit parameter exists, use it!
          Integer limit = limitParameter.getInteger();
        } else {
          // limit parameter doesn't exist (it's not required).
          // If it's required you don't have to check if it's null!
        }
        routingContext.response().setStatusMessage("OK").end();
      });
      // Add a failure handler
      routerFactory.addFailureHandlerByOperationId("listPets", routingContext -> {
        // This is the failure handler
        Throwable failure = routingContext.failure();
        if (failure instanceof ValidationException)
          // Handle Validation Exception
          routingContext.response()
            .setStatusCode(400)
            .setStatusMessage("ValidationException thrown! " + ((ValidationException) failure).type().name())
            .end();
      });

      // Add a security handler
      routerFactory.addSecurityHandler("api_key", routingContext -> {
        // Handle security here
        routingContext.next();
      });

      // Before router creation you can enable/disable various router factory behaviours
      RouterFactoryOptions factoryOptions = new RouterFactoryOptions()
        .setMountValidationFailureHandler(false) // Disable mounting of dedicated validation failure handler
        .setMountResponseContentTypeHandler(true); // Mount ResponseContentTypeHandler automatically

      // Now you have to generate the router
      Router router = routerFactory.setOptions(factoryOptions).getRouter();

      // Now you can use your Router instance
      server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
      server.requestHandler(router).listen((ar) ->  {
        if (ar.succeeded()) {
          logger.info("Server started on port " + ar.result().actualPort());
          future.complete();
        } else {
          logger.error("oops, something went wrong during server initialization", ar.cause());
          future.fail(ar.cause());
        }
      });
    });

  }

  public void stop() {
    this.server.close();
  }

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(OpenAPI3Server.class);
  }

}
