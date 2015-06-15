import groovy.transform.Field
import io.vertx.groovy.ext.unit.TestSuite
import io.vertx.groovy.core.Vertx
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
  client.getNow(8080, "localhost", "/", { resp ->
    resp.bodyHandler({ body ->
      context.assertEquals("foo", body.toString("UTF-8"))
    })
    client.close()
    async.complete()
  })
})
suite.test("some_test2", { context ->
  // Deploy and undeploy a verticle
  vertx.deployVerticle("io.vertx.example.unit.SomeVerticle", context.asyncAssertSuccess({ deploymentID ->
    vertx.undeploy(deploymentID, context.asyncAssertSuccess())
  }))
})

suite.run(options)
