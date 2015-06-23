var Router = require("vertx-web-js/router");

var router = Router.router(vertx);

router.route().blockingHandler(function (routingContext) {
  // Blocking handlers are allowed to block the calling thread
  // So let's simulate a blocking action or long running operation
  try {
    Java.type("java.lang.Thread").sleep(5000);
  } catch(err) {
  }


  // Now call the next handler
  routingContext.next();
}, false);

router.route().handler(function (routingContext) {
  routingContext.response().putHeader("content-type", "text/html").end("Hello World!");
});

vertx.createHttpServer().requestHandler(router.accept).listen(8080);
