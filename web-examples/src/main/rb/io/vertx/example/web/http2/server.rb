require 'vertx-web/router'

image = Java::IoVertxExampleWebHttp2::Image.new($vertx, "coin.png")

router = VertxWeb::Router.router($vertx)

router.get("/").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/html").end(image.generate_html(16))
}

router.get("/img/:x/:y").handler() { |ctx|
  ctx.response().put_header("Content-Type", "image/png").end(image.get_pixel(Java::JavaLang::Integer.parse_int(ctx.path_param("x")), Java::JavaLang::Integer.parse_int(ctx.path_param("y"))))
}

$vertx.create_http_server({
  'ssl' => true,
  'useAlpn' => true,
  'sslEngineOptions' => {
  },
  'pemKeyCertOptions' => {
    'keyPath' => "tls/server-key.pem",
    'certPath' => "tls/server-cert.pem"
  }
}).request_handler(&router.method(:accept)).listen(8443)
