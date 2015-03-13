import io.vertx.groovy.core.Vertx
// Create an HTTP server which simply returns "Hello World!" to each request.
Vertx.vertx().createHttpServer().requestHandler({ req ->
  req.response().end("Hello World!")}).listen(8080)
