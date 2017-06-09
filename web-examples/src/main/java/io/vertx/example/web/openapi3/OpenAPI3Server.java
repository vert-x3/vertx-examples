package io.vertx.example.web.openapi3;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.example.util.Runner;
import io.vertx.example.web.helloworld.Server;
import io.vertx.ext.web.RequestParameters;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.designdriven.OpenAPI3RouterFactory;
import io.vertx.ext.web.validation.ValidationException;


public class OpenAPI3Server extends AbstractVerticle {

    HttpServer server;
    Logger logger = LoggerFactory.getLogger("OpenAPI3RouterFactory");

    public void start() {
        // Load the api spec. This operation is asynchronous
        OpenAPI3RouterFactory.createRouterFactoryFromFile(this.vertx, "src/main/resources/petstore.yaml", openAPI3RouterFactoryAsyncResult -> {
          if (openAPI3RouterFactoryAsyncResult.succeeded()) {
            // Spec loaded with success
            OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();
            // Add an handler with operationId
            routerFactory.addHandlerByOperationId("listPets", routingContext -> {
                // Handle listPets operation
                routingContext.response().setStatusMessage("Called listPets").end();
            }, routingContext -> {
                // This is the failure handler
                Throwable failure = routingContext.failure();
                if (failure instanceof ValidationException)
                    // Handle Validation Exception
                    routingContext.response()
                        .setStatusCode(400)
                        .setStatusMessage("ValidationException thrown! " + ((ValidationException) failure).getErrorType().name())
                        .end();
            });

            // Add an handler with a combination of HttpMethod and path
            routerFactory.addHandler(HttpMethod.POST, "/pets", routingContext -> {
                // Extract request body and use it
                RequestParameters params = routingContext.get("parsedParameters");
                JsonObject pet = params.getBody().getJsonObject();
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(pet.encodePrettily());
            }, routingContext -> {
                Throwable failure = routingContext.failure();
                if (failure instanceof ValidationException)
                    // Handle Validation Exception
                    routingContext.response()
                        .setStatusCode(400)
                        .setStatusMessage("ValidationException thrown! " + ((ValidationException) failure).getErrorType().name())
                        .end();
            });

            // Add a security handler
            routerFactory.addSecurityHandler("api_key", routingContext -> {
                // Handle security here
                routingContext.next();
            });

            // Before router creation you can enable or disable mounting of a default failure handler for ValidationException
            routerFactory.enableValidationFailureHandler(false);

            // Now you have to generate the router
            Router router = routerFactory.getRouter();

            // Now you can use your Router instance
            server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
            server.requestHandler(router::accept).listen();

          } else {
              // Something went wrong during router factory initialization
              Throwable exception = openAPI3RouterFactoryAsyncResult.cause();
              logger.error("oops!", exception);
          }
        });

        logger.info("Server started!");

    }

    public void stop() {
      this.server.close();
    }

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

}
