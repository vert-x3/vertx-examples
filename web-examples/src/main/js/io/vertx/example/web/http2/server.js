var Router = require("vertx-web-js/router");

var image = new (Java.type("io.vertx.example.web.http2.Image"))(vertx, "coin.png");

var router = Router.router(vertx);

router.get("/").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/html").end(image.generateHTML(16));
});

router.get("/img/:x/:y").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "image/png").end(image.getPixel(Java.type("java.lang.Integer").parseInt(ctx.pathParam("x")), Java.type("java.lang.Integer").parseInt(ctx.pathParam("y"))));
});

vertx.createHttpServer({
  "ssl" : true,
  "useAlpn" : true,
  "sslEngine" : 'OPENSSL',
  "pemKeyCertOptions" : {
    "keyPath" : "tls/server-key.pem",
    "certPath" : "tls/server-cert.pem"
  }
}).requestHandler(router.accept).listen(8443);
