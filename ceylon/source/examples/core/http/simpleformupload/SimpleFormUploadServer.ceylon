import io.vertx.ceylon.core { ... }
import io.vertx.ceylon.core.http { ... }

String index = """
                  <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
                        "http://www.w3.org/TR/html4/loose.dtd">
                  <html>
                  <head>
                    <title></title>
                  </head>
                  <body>
                
                  <form action="/form" ENCTYPE="multipart/form-data" method="POST" name="wibble">
                    choose a file to upload:<input type="file" name="myfile"/><br>
                    <input type="submit"/>
                  </form>
                
                  </body>
                  </html>""";

shared class SimpleFormUploadServer() extends Verticle() {
  
  shared actual void start() {
    
    value server = vertx.createHttpServer();
    
    server.requestHandler((req) {
      switch(req.uri())
      case ("/") {
        req.response().putHeader("Content-Type", "text/html").end(index);
      }
      case ("/form") {
        req.setExpectMultipart(true);
        req.uploadHandler((HttpServerFileUpload upload) {
          upload.exceptionHandler((Throwable err) => req.response().end("Upload failed"));
          upload.endHandler(() => req.response().end("Successfully uploaded to ``upload.filename()``"));
          // FIXME - Potential security exploit! In a real system you must check this filename
          // to make sure you're not saving to a place where you don't want!
          // Or better still, just use Vert.x-Web which controls the upload area.
          upload.streamToFileSystem(upload.filename());
        });
      }
      else {
        req.response().setStatusCode(404).end();
      }
      
    }).listen(8080);
  }
}