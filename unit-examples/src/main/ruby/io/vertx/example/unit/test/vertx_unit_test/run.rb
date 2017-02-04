require 'vertx-unit/test_suite'
require 'vertx/vertx'
@vertx

options = {
  'reporters' => [
    {
      'to' => "console"
    }
  ]
}
suite = VertxUnit::TestSuite.create("io.vertx.example.unit.test.VertxUnitTest")

suite.before() { |context|
  @vertx = Vertx::Vertx.vertx()
  @vertx.create_http_server().request_handler() { |req|
    req.response().end("foo")
  }.listen(8080, &context.async_assert_success())
}

suite.after() { |context|
  @vertx.close(&context.async_assert_success())
}

# Specifying the test names seems ugly...
suite.test("some_test1") { |context|
  # Send a request and get a response
  client = @vertx.create_http_client()
  async = context.async()
  client.get_now(8080, "localhost", "/") { |resp|
    resp.body_handler() { |body|
      context.assert_equals("foo", body.to_string("UTF-8"))
    }
    client.close()
    async.complete()
  }
}
suite.test("some_test2") { |context|
  # Deploy and undeploy a verticle
  @vertx.deploy_verticle("io.vertx.example.unit.SomeVerticle", &context.async_assert_success() { |deploymentID|
    @vertx.undeploy(deploymentID, &context.async_assert_success())
  })
}

suite.run(options)
