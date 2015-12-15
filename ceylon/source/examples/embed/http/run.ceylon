import io.vertx.ceylon.core {
  vertx
}
import io.vertx.ceylon.core.http {
  HttpServerRequest,
  HttpServer
}

"Run the module `examples.embed.http`."
shared void run() {
  
  value vertx_ = vertx.vertx();
  value server = vertx_.createHttpServer();
  
  server.requestHandler((HttpServerRequest req) => 
    req.response().putHeader("Content-Type", "text/html").end("<html><body><h1>Hello World</h1></body></html>")
  );
  
  server.listen(8080, (HttpServer|Throwable result) =>
     switch(result)
     case (is HttpServer) print("Server started")
     else result.printStackTrace()
  );
    
}