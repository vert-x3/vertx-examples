import io.vertx.ceylon.core { ... }
import io.vertx.ceylon.core.http { ... }
import java.lang { Thread }

shared class ExecBlocking() extends Verticle() {
  
  shared actual void start() {
    value server = vertx.createHttpServer();
    server.requestHandler((req) => 
      
        // Let's say we have to call a blocking API (e.g. JDBC) to execute a query for each
        // request. We can't do this directly or it will block the event loop
        // But you can do this using executeBlocking:

        vertx.executeBlocking<String>((Future<String> fut) {
      
        // Do the blocking operation in here
      
        // Imagine this was a call to a blocking API to get the result
        Thread.sleep(500);        
        String result = "armadillos!";
        
        fut.complete(result);        
      }, (String?|Throwable res) {
        
        if (is String res) {
          
          req.response().
              putHeader("content-type", "text/plain").
              end(res);
        } else {
          req.response().setStatusCode(500).end("Error");
        }
      })
    ).listen(8080);
  }
}
