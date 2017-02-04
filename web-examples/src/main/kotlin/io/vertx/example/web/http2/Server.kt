package io.vertx.example.web.http2

import io.vertx.ext.web.Router
import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var image = io.vertx.example.web.http2.Image(vertx, "coin.png")

    var router = Router.router(vertx)

    router.get("/").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/html").end(image.generateHTML(16))
    })

    router.get("/img/:x/:y").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "image/png").end(image.getPixel(java.lang.Integer.parseInt(ctx.pathParam("x")), java.lang.Integer.parseInt(ctx.pathParam("y"))))
    })

    vertx.createHttpServer(io.vertx.core.http.HttpServerOptions(
      ssl = true,
      useAlpn = true,
      pemKeyCertOptions = io.vertx.core.net.PemKeyCertOptions(
        keyPath = "tls/server-key.pem",
        certPath = "tls/server-cert.pem"))).requestHandler({ router.accept(it) }).listen(8443)
  }
}
