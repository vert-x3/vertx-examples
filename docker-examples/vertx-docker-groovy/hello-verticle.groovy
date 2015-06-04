vertx.createHttpServer().requestHandler({ request ->
    request.response().end("Groovy world")
}).listen(8080)
