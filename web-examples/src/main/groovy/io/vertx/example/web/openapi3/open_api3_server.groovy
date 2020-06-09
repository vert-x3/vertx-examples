import groovy.transform.Field
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
@Field def server
// Load the api spec. This operation is asynchronous
OpenAPI3RouterFactory.create(this.vertx, "petstore.yaml", { openAPI3RouterFactoryAsyncResult ->
  if (openAPI3RouterFactoryAsyncResult.succeeded()) {
    // Spec loaded with success
    def routerFactory = openAPI3RouterFactoryAsyncResult.result()
    // Add an handler with operationId
    routerFactory.addHandlerByOperationId("listPets", { routingContext ->
      // Load the parsed parameters
      def params = routingContext.get("parsedParameters")
      // Handle listPets operation
      def limitParameter = params.queryParameter("limit")
      if (limitParameter != null) {
        // limit parameter exists, use it!
        def limit = limitParameter.getInteger()
      } else {
        // limit parameter doesn't exist (it's not required).
        // If it's required you don't have to check if it's null!
      }
      routingContext.response().setStatusMessage("OK").end()
    })
    // Add a failure handler
    routerFactory.addFailureHandlerByOperationId("listPets", { routingContext ->
      // This is the failure handler
      def failure = routingContext.failure()
      if (failure instanceof io.vertx.ext.web.api.validation.ValidationException) {
        routingContext.response().setStatusCode(400).setStatusMessage("ValidationException thrown! ${failure.type().name()}").end()}
    })

    // Add a security handler
    routerFactory.addSecurityHandler("api_key", { routingContext ->
      // Handle security here
      routingContext.next()
    })

    // Before router creation you can enable/disable various router factory behaviours
    def factoryOptions = [
      mountValidationFailureHandler:false,
      mountResponseContentTypeHandler:true
    ]

    // Now you have to generate the router
    def router = routerFactory.setOptions(factoryOptions).getRouter()

    // Now you can use your Router instance
    server = vertx.createHttpServer([
      port:8080,
      host:"localhost"
    ])
    server.requestHandler(router).listen({ ar ->
      if (ar.succeeded()) {
        println("Server started on port ${ar.result().actualPort()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  } else {
    // Something went wrong during router factory initialization
    openAPI3RouterFactoryAsyncResult.cause().printStackTrace()
  }
})
