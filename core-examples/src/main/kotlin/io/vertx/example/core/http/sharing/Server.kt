import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.deployVerticle("io.vertx.example.core.http.sharing.HttpServerVerticle", io.vertx.core.DeploymentOptions(
      instances = 2))
  }
}
