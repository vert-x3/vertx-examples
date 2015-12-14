import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.web.handler { ... }
import io.vertx.ceylon.core { Verticle }
import io.vertx.ceylon.web.templ { mvelTemplateEngine }

shared class Server() extends Verticle() {
  
  shared actual void start() {
    
    value router_ = router.router(vertx);

    // Serve the dynamic pages
    router_.route("/dynamic/*").handler(templateHandler.create(mvelTemplateEngine.create()).handle);
    
    // Serve the static pages
    router_.route().handler(staticHandler.create().handle);

    vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
  }

}