package io.vertx.example.core.http.httpssni;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.example.util.Runner;

public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpClient(new HttpClientOptions()
        .setSsl(true)
        .setTrustOptions(new JksOptions().setPath("server-virt-keystore.jks").setPassword("wibble"))
        .setSniServerName("my.example.com")).getNow(4443, "localhost", "/", resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
    });
  }
}
