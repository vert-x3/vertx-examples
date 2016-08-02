import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.web.handler { ... }
import io.vertx.ceylon.core { Verticle }
import io.vertx.ceylon.web.templ { thymeleafTemplateEngine }
import io.vertx.ceylon.core.buffer { Buffer }

shared class Server() extends Verticle() {
  
  shared actual void start() {
    
    // In order to use a template we first need to create an engine
    value engine = thymeleafTemplateEngine.create();
    
    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    value router_ = router.router(vertx);

    // Entry point to the application, this will render a custom template.
    router_.get().handler {
      void requestHandler(RoutingContext ctx) {
        
        // we define a hardcoded title for our application
        ctx.put("welcome", "Hi there!");
        
        //
        engine.render(ctx, "templates/index.html", (Buffer|Throwable res) {
          switch(res)
          case (is Buffer) {
            ctx.response().end(res);
          }
          else {
            ctx.fail(res);
          }
        });
      }
    };

    // start a HTTP web server on port 8080
    vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
  }

}
