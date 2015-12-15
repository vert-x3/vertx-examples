import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.web.handler { ... }
import io.vertx.ceylon.core { Verticle }

shared class Server() extends Verticle() {
  
  shared actual void start() {    
    value router_ = router.router(vertx);
    
    // Enable multipart form data parsing
    router_.route().handler(bodyHandler.create().handle);
    
    router_.route("/").handler {
      void requestHandler(RoutingContext routingContext) => routingContext.response().
          putHeader("Content-type", "text/html").
          end("""<form action="/form" method="post">
                   <div>
                     <label for="name">Enter your name:</label>
                     <input type="text" id="name" name="name" />
                   </div>
                   <div class="button">
                     <button type="submit">Send</button>
                   </div>
                 </form>""");
    };
    
    // handle the form
    router_.post("/form").handler {
      void requestHandler(RoutingContext ctx) {
        ctx.response().putHeader("Content-type", "text/plain");
        // note the form attribute matches the html form element name.
        assert(exists name = ctx.request().getParam("name"));
        ctx.response().end("Hello ``name``!");
      }
    };
    
    vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
  }
}