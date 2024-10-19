package io.vertx.example.webclient.send.stream;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private WebClient client;

  @Override
  public Future<?> start() throws Exception {

    String filename = "io/vertx/example/webclient/send/stream/upload.txt";
    FileSystem fs = vertx.fileSystem();

    client = WebClient.create(vertx);

    return fs.props(filename)
      .onComplete(ares -> {
      FileProps props = ares.result();
      System.out.println("props is " + props);
      long size = props.size();

      HttpRequest<Buffer> req = client.put(8080, "localhost", "/");
      req.putHeader("content-length", "" + size);

      fs.open(filename, new OpenOptions())
        .compose(ares2 -> req.sendStream(ares2))
        .onSuccess(response -> System.out.println("Got HTTP response with status " + response.statusCode()));
    });
  }
}
