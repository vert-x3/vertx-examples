require 'vertx-unit/test_suite'
require 'vertx/vertx'

options = {
  'reporters' => [
    {
      'to' => "console"
    }
  ]
}
suite = VertxUnit::TestSuite.create("io.vertx.example.unit.test.VertxUnitTest")

suite.before() { |context|
  $vertx = Vertx::Vertx.vertx()
  async = context.async()
  server = $vertx.create_http_server().request_handler() { |req|
    req.response().end("foo")}.listen(8080) { |res,res_err|
    if (res_err == nil)
      async.complete()
    else
      context.fail()
    end
  }
}

suite.after() { |context|
  async = context.async()
  $vertx.close() { |ar,ar_err|
    if (ar_err == nil)
      async.complete()
    else
      if (ar_err != nil)
        context.fail()
      endend
  }
}

# Specifying the test names seems ugly...
suite.test("some_test1") { |context|
  # Send a request and get a response
  client = $vertx.create_http_client()
  async = context.async()
  client.get_now(8080, "localhost", "/") { |resp|
    resp.body_handler() { |body|
      context.assert_equals("foo", body.to_string("UTF-8"))}
    client.close()
    async.complete()
  }
}
suite.test("some_test2") { |context|
  # Deploy and undeploy a verticle
  async = context.async()
  $vertx.deploy_verticle("io.vertx.example.unit.SomeVerticle") { |res,res_err|
    if (res_err == nil)
      deploymentID = res
      $vertx.undeploy(deploymentID) { |res2,res2_err|
        if (res2_err == nil)
          async.complete()
        else
          context.fail()
        end
      }
    else
      context.fail()
    end
  }
}

suite.run(options)

