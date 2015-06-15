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
  vertx.createHttpServer().requestHandler(function (req) {
    req.response().end("foo");
  }).listen(8080, context.asyncAssertSuccess());
});

suite.after(function (context) {
  vertx.close(context.asyncAssertSuccess());
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
  vertx.deployVerticle("io.vertx.example.unit.SomeVerticle", context.asyncAssertSuccess(function (deploymentID) {
    vertx.undeploy(deploymentID, context.asyncAssertSuccess());
  }));
});

suite.run(options);
