package io.vertx.example.core.http.simpleformupload;

import io.vertx.core.AbstractVerticle;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleFormUploadServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      if (req.uri().equals("/")) {
        // Serve the index page
        req.response().sendFile("simpleformupload/index.html");
      } else if (req.uri().startsWith("/form")) {
        req.setExpectMultipart(true);
        req.uploadHandler(upload -> {
          upload.exceptionHandler(cause -> {
            req.response().setChunked(true).end("Upload failed");
          });

          upload.endHandler(v -> {
            req.response().setChunked(true).end("Upload successful, you should see the file in the server directory");
          });
          upload.streamToFileSystem(upload.filename());
        });
      } else {
        req.response().setStatusCode(404);
        req.response().end();
      }
    }).listen(8080);

  }
}
