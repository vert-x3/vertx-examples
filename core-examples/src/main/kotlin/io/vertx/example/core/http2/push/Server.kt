import io.vertx.core.http.HttpMethod
import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var server = vertx.createHttpServer(io.vertx.core.http.HttpServerOptions(
      useAlpn = true,
      ssl = true,
      pemKeyCertOptions = io.vertx.core.net.PemKeyCertOptions(
        keyPath = "server-key.pem",
        certPath = "server-cert.pem")))

    server.requestHandler({ req ->
      var path = req.path()
      var resp = req.response()
      if ("/" == path) {
        resp.push(HttpMethod.GET, "/script.js", { ar ->
          if (ar.succeeded()) {
            println("sending push")
            var pushedResp = ar.result()
            pushedResp.sendFile("script.js")
          } else {
            // Sometimes Safari forbids push : "Server push not allowed to opposite endpoint."
          }
        })

        resp.sendFile("index.html")
      } else if ("/script.js" == path) {
        resp.sendFile("script.js")
      } else {
        println("Not found ${path}")
        resp.setStatusCode(404).end()
      }
    })

    server.listen(8443, "localhost", { ar ->
      if (ar.succeeded()) {
        println("Server started")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
