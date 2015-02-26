package io.vertx.example.core.execblocking;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ExecBlockingExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ExecBlockingExample.class);
  }

  @CodeTranslate
  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(request -> {

      // Let's say we have to call a blocking API (e.g. JDBC) to execute a query for each
      // request. We can't do this directly or it will block the event loop
      // But you can do this using executeBlocking:

      vertx.<String>executeBlocking(future -> {

        // Do the blocking operation in here

        // Imagine this was a call to a blocking API to get the result
        try {
          Thread.sleep(500);
        } catch (Exception ignore) {
        }
        String result = "armadillos!";

        future.complete(result);

      }, res -> {

        if (res.succeeded()) {

          request.response().putHeader("content-type", "text/plain").end(res.result());

        } else {
          res.cause().printStackTrace();
        }
      });

    }).listen(8080);

  }
}
