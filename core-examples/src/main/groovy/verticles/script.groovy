/**
 * A verticle developed as a plain groovy script. The code is executed in the Vert.x Event Loop
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */


println "Starting from " + Thread.currentThread().name

vertx.createHttpServer().requestHandler({ request ->
  request.response().end("Hello world")
}).listen(8080)
