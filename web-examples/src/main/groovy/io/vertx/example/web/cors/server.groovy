import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.StaticHandler

def router = Router.router(vertx)

def allowedHeaders = new java.util.HashSet()
allowedHeaders.add("x-requested-with")
allowedHeaders.add("Access-Control-Allow-Origin")
allowedHeaders.add("origin")
allowedHeaders.add("Content-Type")
allowedHeaders.add("accept")
allowedHeaders.add("X-PINGARUNER")

def allowedMethods = new java.util.HashSet()
allowedMethods.add(HttpMethod.GET)
allowedMethods.add(HttpMethod.POST)
allowedMethods.add(HttpMethod.OPTIONS)
/*
 * these methods aren't necessary for this sample, 
 * but you may need them for your projects
 */
allowedMethods.add(HttpMethod.DELETE)
allowedMethods.add(HttpMethod.PATCH)
allowedMethods.add(HttpMethod.PUT)

router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods))

router.get("/access-control-with-get").handler({ ctx ->
  def httpServerResponse = ctx.response()
  httpServerResponse.setChunked(true)
  def headers = ctx.request().headers()
  headers.names().each { key ->
    httpServerResponse.write("${key}: ")
    httpServerResponse.write(headers.get(key))
    httpServerResponse.write("<br>")
  }
  httpServerResponse.putHeader("Content-Type", "application/text").end("Success")
})

router.post("/access-control-with-post-preflight").handler({ ctx ->
  def httpServerResponse = ctx.response()
  httpServerResponse.setChunked(true)
  def headers = ctx.request().headers()
  headers.names().each { key ->
    httpServerResponse.write("${key}: ")
    httpServerResponse.write(headers.get(key))
    httpServerResponse.write("<br>")
  }
  httpServerResponse.putHeader("Content-Type", "application/text").end("Success")
})

// Serve the static resources
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router).listen(8080)
