import io.vertx.examples.service.groovy.ProcessorService
def service = ProcessorService.createProxy(vertx, "vertx.processor")

def document = [
  name:"vertx"
]

service.process(document, { r ->
  if (r.succeeded()) {
    println(groovy.json.JsonOutput.toJson(r.result()))
  } else {
    println(r.cause())
  }
})
