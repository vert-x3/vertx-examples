package io.vertx.example.web.validation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.RequestParameter;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.ext.web.api.validation.ParameterTypeValidator;
import io.vertx.ext.web.api.validation.ValidationException;
import io.vertx.ext.web.handler.BodyHandler;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ValidationExampleServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    // If you want to validate bodies, don't miss that handler!
    router.route().handler(BodyHandler.create());

    // Create Validation Handler with some stuff
    HTTPRequestValidationHandler validationHandler =
      HTTPRequestValidationHandler.create()
        .addQueryParam("parameterName", ParameterType.INT, true)
        .addFormParamWithPattern("formParameterName", "a{4}", true)
        .addPathParam("pathParam", ParameterType.FLOAT);

    router.post("/hello/:pathParam")
      // Mount validation handler
      .handler(validationHandler)
      //Mount your handler
      .handler((routingContext) -> {
        // To consume parameters you can get it in the "classic way" of Vert.x Web
        // or you can use the RequestParameters that contains parameters already parsed (and maybe transformed)
        // Get RequestParameters container
        RequestParameters params = routingContext.get("parsedParameters");

        // Get parameters
        Integer parameterName = params.queryParameter("parameterName").getInteger();
        String formParameterName = params.formParameter("formParameterName").getString();
        Float pathParam = params.pathParameter("pathParam").getFloat();
        // Do awesome things with your parameters!
      })
      //Mount your failure handler
      .failureHandler((routingContext) -> {
        Throwable failure = routingContext.failure();
        if (failure instanceof ValidationException) {
          // Something went wrong during validation!
          String validationErrorMessage = failure.getMessage();
          routingContext.response().setStatusCode(400).end();
        }
      });

    // A very basic example of JSON body validation
    router.post("/jsonUploader")
      .handler(HTTPRequestValidationHandler.create().addJsonBodySchema("{type: string}"))
      .handler((routingContext -> {
        RequestParameters params = routingContext.get("parsedParameters");
        JsonObject body = params.body().getJsonObject();
      }));

    // Write your own parameter type validator
    ParameterTypeValidator primeCustomTypeValidator = value -> {
      try {
        int number = Integer.parseInt(value);
        // Yeah I know this is a lazy way to check if number is prime :)
        for(int i = 2; i < number; i++) {
          if(number % i == 0) // Number is not prime
            throw ValidationException.ValidationExceptionFactory.generateNotMatchValidationException("Number is not prime!");
        }
        return RequestParameter.create(number); // We pass directly the parsed parameter
      } catch(NumberFormatException e){
        throw ValidationException.ValidationExceptionFactory.generateNotMatchValidationException("Wrong integer format");
      }
    };

    // Now we create the route and mount the HTTPRequestValidationHandler
    router.get("/superAwesomeParameter")
      .handler(HTTPRequestValidationHandler.create()
        // Mount the custom type validator
        .addQueryParamWithCustomTypeValidator("primeNumber", primeCustomTypeValidator, true, false))
      .handler((routingContext -> {
        RequestParameters params = routingContext.get("parsedParameters");
        Integer primeNumber = params.queryParameter("primeNumber").getInteger();
      }));

    vertx.createHttpServer().requestHandler(router::accept).listen();
  }

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ValidationExampleServer.class);
  }
}
