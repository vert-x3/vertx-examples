package io.vertx.example.web.validation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.ParameterProcessorFactory;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;

import static io.vertx.ext.web.validation.builder.Parameters.*;
import static io.vertx.ext.web.validation.builder.Bodies.*;
import static io.vertx.json.schema.draft7.dsl.Schemas.*;

/**
 * @author <a href="https://slinkydeveloper.com">Francesco Guardiani</a>
 */
public class ValidationExampleServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    // The schema parser is required to create new schemas
    SchemaParser parser = SchemaParser.createDraft7SchemaParser(
      SchemaRouter.create(vertx, new SchemaRouterOptions())
    );

    Router router = Router.router(vertx);

    // If you want to validate bodies, don't miss that handler!
    router.route().handler(BodyHandler.create());

    // Create Validation Handler with some stuff
    ValidationHandler validationHandler = ValidationHandler.builder(parser)
      .queryParameter(param("parameterName", intSchema()))
      .pathParameter(param("pathParam", numberSchema()))
      .body(
        formUrlEncoded(
          objectSchema()
            .property("r", intSchema())
            .property("g", intSchema())
            .property("b", intSchema())
        )
      )
      .build();

    router.post("/hello/:pathParam")
      // Mount validation handler
      .handler(validationHandler)
      //Mount your handler
      .handler((routingContext) -> {
        // To consume parameters you can get it in the "classic way" of Vert.x Web
        // or you can use the RequestParameters that contains parameters already parsed (and maybe transformed)
        // Get RequestParameters container
        RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        // Get parameters
        Integer parameterName = params.queryParameter("parameterName").getInteger();
        Float pathParam = params.pathParameter("pathParam").getFloat();

        // Get body
        JsonObject rgbObject = params.body().getJsonObject();

        // Do awesome things with your parameters!
      });

    // A very basic example of JSON body validation
    router.post("/jsonUploader")
      .handler(ValidationHandler.builder(parser)
        .body(json(
          objectSchema()
            .additionalProperties(stringSchema())
        ))
        .build()
      )
      .handler((routingContext -> {
        RequestParameters params = routingContext.get("parsedParameters");
        JsonObject body = params.body().getJsonObject();
      }));

    // Custom handler for the ValidationException
    router.errorHandler(400, routingContext -> {
      Throwable failure = routingContext.failure();
      if (failure instanceof BadRequestException) {
        // Something went wrong during validation!
        String validationErrorMessage = failure.getMessage();
        routingContext.response().setStatusCode(400).end();
      }
    });

    vertx.createHttpServer().requestHandler(router).listen();
  }

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ValidationExampleServer.class);
  }
}
