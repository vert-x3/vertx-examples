package io.vertx.example.unit.test.VertxUnitTest

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestOptions
import io.vertx.ext.unit.TestSuite
import io.vertx.ext.unit.report.ReportOptions
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.ext.unit.*
import io.vertx.kotlin.ext.unit.report.*

class run : io.vertx.core.AbstractVerticle()  {
  var vertx: Vertx
  override fun start() {

    var options = TestOptions(
      reporters = listOf(ReportOptions(
        to = "console")))
    var suite = TestSuite.create("io.vertx.example.unit.test.VertxUnitTest")

    suite.before({ context ->
      vertx = Vertx.vertx()
      vertx.createHttpServer().requestHandler({ req ->
        req.response().end("foo")
      }).listen(8080, context.asyncAssertSuccess<Any>())
    })

    suite.after({ context ->
      vertx.close(context.asyncAssertSuccess<Any>())
    })

    // Specifying the test names seems ugly...
    suite.test("some_test1", { context ->
      // Send a request and get a response
      var client = vertx.createHttpClient()
      var async = context.async()
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
        vertx.undeploy(deploymentID, context.asyncAssertSuccess<Any>())
      }))
    })

    suite.run(options)
  }
}
