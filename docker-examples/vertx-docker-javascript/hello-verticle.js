vertx.createHttpServer().requestHandler(function (request) {
    request.response().end("Wild world");
}).listen(8080);
