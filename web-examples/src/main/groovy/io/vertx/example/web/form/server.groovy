import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler

def router = Router.router(vertx)

// Enable multipart form data parsing
router.route().handler(BodyHandler.create())

router.route("/").handler({ routingContext ->
  routingContext.response().putHeader("content-type", "text/html").end("<form action=\"/form\" method=\"post\">\n    <div>\n        <label for=\"name\">Enter your name:</label>\n        <input type=\"text\" id=\"name\" name=\"name\" />\n    </div>\n    <div class=\"button\">\n        <button type=\"submit\">Send</button>\n    </div></form>")
})

// handle the form
router.post("/form").handler({ ctx ->
  ctx.response().putHeader(io.vertx.core.http.HttpHeaders.CONTENT_TYPE, "text/plain")
  // note the form attribute matches the html form element name.
  ctx.response().end("Hello ${ctx.request().getParam("name")}!")
})

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
