import io.vertx.core.AbstractVerticle

/**
 * A verticle developed as class extending `AbstractVerticle`.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */

class MyVerticle extends AbstractVerticle {

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


