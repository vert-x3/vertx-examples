import io.vertx.ext.web.Router
import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    router.route().handler({ routingContext ->
      routingContext.response().putHeader("content-type", "text/html").end("Hello World!")
    })

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
