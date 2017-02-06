package io.vertx.example.webclient.send.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

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
}
