import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.web.handler { ... }
import io.vertx.ceylon.core { Verticle }

shared class Server() extends Verticle() {
  
  shared actual void start() {    
    value router_ = router.router(vertx);

    // Serve the static pages
    router_.route().handler(staticHandler.create().handle);
    
    vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
    
    print("Server is started");
  }
}