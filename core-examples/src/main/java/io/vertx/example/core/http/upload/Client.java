package io.vertx.example.core.http.upload;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.streams.Pump;
import io.vertx.example.util.Runner;

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
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    client.request(HttpMethod.PUT, 8080, "localhost", "/someurl")
      .compose(req -> {
        String filename = "upload.txt";
        FileSystem fs = vertx.fileSystem();
        return fs.props(filename).compose(props -> {
          System.out.println("props is " + props);
          long size = props.size();
          req.headers().set("content-length", "" + size);
          return fs.open(filename, new OpenOptions());
        }).compose(file -> req.send(file)
          .map(resp -> resp.statusCode()));
      }).onSuccess(statusCode -> {
      System.out.println("Response " + statusCode);
    }).onFailure(err -> {
      err.printStackTrace();
    });
  }
}
