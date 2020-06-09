import groovy.transform.Field
import io.vertx.ext.unit.TestSuite
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClientResponse
@Field def vertx

def options = [
  reporters:[
    [
      to:"console"
    ]
  ]
]
def suite = TestSuite.create("io.vertx.example.unit.test.VertxUnitTest")

suite.before({ context ->
  vertx = Vertx.vertx()
  vertx.createHttpServer().requestHandler({ req ->
    req.response().end("foo")
  }).listen(8080, context.asyncAssertSuccess())
})

suite.after({ context ->
  vertx.close(context.asyncAssertSuccess())
})

// Specifying the test names seems ugly...
suite.test("some_test1", { context ->
  // Send a request and get a response
  def client = vertx.createHttpClient()
  def async = context.async()
  client.get(8080, "localhost", "/").flatMap(HttpClientResponse.&body).onSuccess({ body ->
    context.assertEquals("foo", body.toString("UTF-8"))
    client.close()
    async.complete()
  }).onFailure(context.&fail)
})
suite.test("some_test2", { context ->
  // Deploy and undeploy a verticle
  vertx.deployVerticle("io.vertx.example.unit.SomeVerticle", context.asyncAssertSuccess({ deploymentID ->
    vertx.undeploy(deploymentID, context.asyncAssertSuccess())
  }))
})

suite.run(options)
