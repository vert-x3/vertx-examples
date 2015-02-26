package io.vertx.example.util;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.util.function.Consumer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ExampleRunner {


  public static void runJavaExample(String prefix, Class clazz, boolean clustered) {
    String exampleDir = prefix + clazz.getPackage().getName().replace(".", "/");
    runExample(exampleDir, clazz.getName(), clustered);
  }


  public static void runJSExample(String prefix, String scriptName, boolean clustered) {
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    System.out.println("scriptDir is " + scriptDir);
    ExampleRunner.runExample(scriptDir, scriptDir + "/" + file.getName(), clustered);
  }

  public static void runExample(String exampleDir, String verticleID, boolean clustered) {
    System.setProperty("vertx.cwd", exampleDir);
    Consumer<Vertx> runner = vertx -> {
      try {
        vertx.deployVerticle(verticleID);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    };
    if (clustered) {
      Vertx.clusteredVertx(new VertxOptions().setClustered(true), res -> {
        if (res.succeeded()) {
          Vertx vertx = res.result();
          runner.accept(vertx);
        } else {
          res.cause().printStackTrace();
        }
      });
    } else {
      Vertx vertx = Vertx.vertx();
      runner.accept(vertx);
    }
  }

//  static class EchoServerRunner {
//    public static void main(String[] args) {
//      run(Server.class);
//    }
//  }
//
//  static class EchoClientRunner {
//    public static void main(String[] args) {
//      run(Client.class);
//    }
//  }
//
//  static class SendFileRunner {
//    public static void main(String[] args) {
//      run(SendFile.class);
//    }
//  }
//
//  // JS
//
//  static class EchoServerRunnerJS {
//    public static void main(String[] args) {
//      run("core-examples/src/main/js/echo/echo_server.js");
//    }
//  }
}
