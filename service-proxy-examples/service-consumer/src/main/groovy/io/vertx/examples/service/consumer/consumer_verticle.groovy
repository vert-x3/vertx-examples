import io.vertx.examples.service.groovy.ProcessorService
def service = ProcessorService.createProxy(vertx, "vertx.processor")

def document = [
  name:"vertx"
]

service.process(document, { r ->
  if (r.succeeded()) {
    println(groovy.json.JsonOutput.toJson(r.result()))
  } else {
    if (r.cause() instanceof io.vertx.serviceproxy.ServiceException) {
      def exc = r.cause()
      if (exc.failureCode() == BAD_NAME_ERROR) {
        println("Failed to process the document: The name in the document is bad. The name provided is: ${exc.getDebugInfo().name}")
      } else if (exc.failureCode() == NO_NAME_ERROR) {
        println("Failed to process the document: No name was found")
      }
    } else {
      println("Unexpected error: ${r.cause()}")

    }
  }
})
