/**
 * A verticle developed as a groovy script with hooks. vert.x calls the `vertxStart` and `vertxStop` method when the verticle
 * is deployed and un-deployed.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */

void vertxStart() {
  println "starting"
  vertx.createHttpServer().requestHandler({ request ->
    request.response().end("Hello world")
  }).listen(8080)

}

void vertxStop() {
  println "stopping"
}

