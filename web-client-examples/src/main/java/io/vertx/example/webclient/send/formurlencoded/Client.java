package io.vertx.example.webclient.send.formurlencoded;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.example.util.Runner;
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
      System.out.println("Got form with content-type " + req.getHeader("content-type"));
      req.setExpectMultipart(true);
      req.endHandler(v -> {
        System.out.println("firstName: " + req.getFormAttribute("firstName"));
        System.out.println("lastName: " + req.getFormAttribute("lastName"));
        System.out.println("male: " + req.getFormAttribute("male"));
      });

    }).listen(8080, listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {

        WebClient client = WebClient.create(vertx);

        MultiMap form = MultiMap.caseInsensitiveMultiMap();
        form.add("firstName", "Dale");
        form.add("lastName", "Cooper");
        form.add("male", "true");

        client.post(8080, "localhost", "/").sendForm(form, ar -> {
          if (ar.succeeded()) {
            HttpResponse<Buffer> response = ar.result();
            System.out.println("Got HTTP response with status " + response.statusCode());
          } else {
            ar.cause().printStackTrace();
          }
        });
      }
    });
  }
}
