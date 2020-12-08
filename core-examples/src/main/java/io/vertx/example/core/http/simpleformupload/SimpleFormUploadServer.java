package io.vertx.example.core.http.simpleformupload;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * NOTE! It's recommended to use Vert.x-Web for handling file uploads otherwise it's easy to get caught
 * by malicious requests which might craft an upload to save it in a place where you don't want it to be saved.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleFormUploadServer extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SimpleFormUploadServer.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      if (req.uri().equals("/")) {
        // Serve the index page
        req.response().sendFile("index.html");
      } else if (req.uri().startsWith("/form")) {
        req.setExpectMultipart(true);
        req.uploadHandler(upload -> {
          // FIXME - Potential security exploit! In a real system you must check this filename
          // to make sure you're not saving to a place where you don't want!
          // Or better still, just use Vert.x-Web which controls the upload area.
          upload.streamToFileSystem(upload.filename())
            .onSuccess(v -> req.response().end("Successfully uploaded to " + upload.filename()))
            .onFailure(err -> req.response().end("Upload failed"));
        });
      } else {
        req.response()
          .setStatusCode(404)
          .end();
      }
    }).listen(8080);

  }
}
