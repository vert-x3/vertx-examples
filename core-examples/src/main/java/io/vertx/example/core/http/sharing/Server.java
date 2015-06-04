package io.vertx.example.core.http.sharing;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/**
 * An example illustrating the server sharing and round robin. The servers are identified using an id.
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    getVertx().deployVerticle(new HttpVerticle("server-1"));
    getVertx().deployVerticle(new HttpVerticle("server-2"));
  }


  private class HttpVerticle extends AbstractVerticle {

    private final String id;

    private HttpVerticle(String id) {
      this.id = id;
    }


    @Override
    public void start() throws Exception {
      vertx.createHttpServer().requestHandler(req -> {
        req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from " +
            id + "</h1></body></html>");
      }).listen(8080);
    }
  }
}
