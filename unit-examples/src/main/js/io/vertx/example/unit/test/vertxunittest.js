var TestSuite = require("vertx-unit-js/test_suite");
var Vertx = require("vertx-js/vertx");
var vertx;

var options = {
  "reporters" : [
    {
      "to" : "console"
    }
  ]
};
var suite = TestSuite.create("io.vertx.example.unit.test.VertxUnitTest");

suite.before(function (context) {
  vertx = Vertx.vertx();
  var async = context.async();
  var server = vertx.createHttpServer().requestHandler(function (req) {
    req.response().end("foo");
  }).listen(8080, function (res, res_err) {
    if (res_err == null) {
      async.complete();
    } else {
      context.fail();
    }
  });
});

suite.after(function (context) {
  var async = context.async();
  vertx.close(function (ar, ar_err) {
    if (ar_err == null) {
      async.complete();
    } else if (ar_err != null) {
      context.fail();
    }
  });
});

// Specifying the test names seems ugly...
suite.test("some_test1", function (context) {
  // Send a request and get a response
  var client = vertx.createHttpClient();
  var async = context.async();
  client.getNow(8080, "localhost", "/", function (resp) {
    resp.bodyHandler(function (body) {
      context.assertEquals("foo", body.toString("UTF-8"));
    });
    client.close();
    async.complete();
  });
});
suite.test("some_test2", function (context) {
  // Deploy and undeploy a verticle
  var async = context.async();
  vertx.deployVerticle("io.vertx.example.unit.SomeVerticle", function (res, res_err) {
    if (res_err == null) {
      var deploymentID = res;
      vertx.undeploy(deploymentID, function (res2, res2_err) {
        if (res2_err == null) {
          async.complete();
        } else {
          context.fail();
        }
      });
    } else {
      context.fail();
    }
  });
});

suite.run(options);

