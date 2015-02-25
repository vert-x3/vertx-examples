package io.vertx.example.core;

import io.vertx.core.Vertx;
import io.vertx.example.core.echo.EchoClient;
import io.vertx.example.core.echo.EchoServer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  static void run(Class clazz) {
    run(clazz.getName());
  }

  static void run(String verticleID) {
    try {
      Vertx.vertx().deployVerticle(verticleID);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  static class EchoServerRunner {
    public static void main(String[] args) {
      run(EchoServer.class);
    }
  }

  static class EchoClientRunner {
    public static void main(String[] args) {
      run(EchoClient.class);
    }
  }

  // JS

  static class EchoServerRunnerJS {
    public static void main(String[] args) {
      run("core-examples/src/main/js/echo/echo_server.js");
    }
  }
}
