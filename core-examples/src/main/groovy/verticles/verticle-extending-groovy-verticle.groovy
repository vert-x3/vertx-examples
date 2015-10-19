import io.vertx.lang.groovy.GroovyVerticle

/**
 * A verticle developed as class extending `GroovyVerticle`.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */

class MyVerticle extends GroovyVerticle {

  @Override
  void start() throws Exception {
    println "starting"

    vertx.createHttpServer().requestHandler({ request ->
      request.response().end("Hello world")
    }).listen(8080)
  }

  @Override
  void stop() throws Exception {
    println "stopping"
  }
}


