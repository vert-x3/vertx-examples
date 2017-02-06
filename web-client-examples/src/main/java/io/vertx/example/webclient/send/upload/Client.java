package io.vertx.example.webclient.send.upload;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.Pump;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(req -> {

      req.bodyHandler(buff -> {
        System.out.println("Receiving user " + buff + " from client ");
        req.response().end();
      });

    }).listen(8080, listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {

        String filename = "upload.txt";
        FileSystem fs = vertx.fileSystem();

        WebClient client = WebClient.create(vertx);

        fs.props(filename, ares -> {
          FileProps props = ares.result();
          System.out.println("props is " + props);
          long size = props.size();

          HttpRequest<Buffer> req = client.put(8080, "localhost", "/");
          req.putHeader("content-length", "" + size);

          fs.open(filename, new OpenOptions(), ares2 -> {
            req.sendStream(ares2.result(), ar -> {
              if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                System.out.println("Got HTTP response with status " + response.statusCode());
              } else {
                ar.cause().printStackTrace();
              }
            });
          });
        });
      }
    });
  }
}
