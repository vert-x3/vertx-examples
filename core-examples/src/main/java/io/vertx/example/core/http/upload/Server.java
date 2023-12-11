package io.vertx.example.core.http.upload;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.streams.Pipe;

import java.util.UUID;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      Pipe<Buffer> pipe = req
        .pipe()
        .endOnComplete(true);
      String filename = UUID.randomUUID() + ".uploaded";
      vertx.fileSystem()
        .open(filename, new OpenOptions())
        .transform(ar -> {
          if (ar.succeeded()) {
            return pipe.to(ar.result()).onComplete(ar2 -> {
              System.out.println("Uploaded to " + filename);
            });
          } else {
            return req.response()
              .setStatusCode(500)
              .end("Could not upload file");
          }
        });
    }).listen(8080);
  }
}
