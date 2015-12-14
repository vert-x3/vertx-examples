import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.core { Verticle }

shared class Server() extends Verticle() {
  
  shared actual void start() {    
    value router_ = router.router(vertx);
    router_.route().handler((RoutingContext routingContext) => 
      routingContext.response().putHeader("content-type", "text/html").end("Hello World!")
    );
    vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
  }
}