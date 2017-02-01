import io.vertx.core.http.HttpVersion
import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = io.vertx.core.http.HttpClientOptions(
      protocolVersion = "HTTP_2")

    vertx.createHttpClient(options).getNow(8080, "localhost", "/", { resp ->
      println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
      resp.bodyHandler({ body ->
        println("Got data ${body.toString("ISO-8859-1")}")
      })
    })
  }
}
