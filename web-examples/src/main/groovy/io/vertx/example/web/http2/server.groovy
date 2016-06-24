import io.vertx.groovy.ext.web.Router

def image = new io.vertx.example.web.http2.Image(vertx, "coin.png")

def router = Router.router(vertx)

router.get("/").handler({ ctx ->
  ctx.response().putHeader("Content-Type", "text/html").end(image.generateHTML(16))
})

router.get("/img/:x/:y").handler({ ctx ->
  ctx.response().putHeader("Content-Type", "image/png").end(image.getPixel(java.lang.Integer.parseInt(ctx.pathParam("x")), java.lang.Integer.parseInt(ctx.pathParam("y"))))
})

vertx.createHttpServer([
  ssl:true,
  useAlpn:true,
  pemKeyCertOptions:[
    keyPath:"tls/server-key.pem",
    certPath:"tls/server-cert.pem"
  ]
]).requestHandler(router.&accept).listen(8443)
