import io.vertx.groovy.ext.unit.TestSuite
import io.vertx.groovy.core.Vertx

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
  def async = context.async()
  def server = vertx.createHttpServer().requestHandler({ req ->
    req.response().end("foo")
  }).listen(8080, { res ->
    if (res.succeeded()) {
      async.complete()
    } else {
      context.fail()
    }
  })
})

suite.after({ context ->
  def async = context.async()
  vertx.close({ ar ->
    if (ar.succeeded()) {
      async.complete()
    } else if (ar.failed()) {
      context.fail()
    }
  })
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
  def async = context.async()
  vertx.deployVerticle("io.vertx.example.unit.SomeVerticle", { res ->
    if (res.succeeded()) {
      def deploymentID = res.result()
      vertx.undeploy(deploymentID, { res2 ->
        if (res2.succeeded()) {
          async.complete()
        } else {
          context.fail()
        }
      })
    } else {
      context.fail()
    }
  })
})

suite.run(options)

