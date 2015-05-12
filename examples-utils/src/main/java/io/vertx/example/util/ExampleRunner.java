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
    runJavaExample(prefix, clazz, new VertxOptions().setClustered(clustered));
  }

  public static void runJavaExample(String prefix, Class clazz, VertxOptions options) {
    String exampleDir = prefix + clazz.getPackage().getName().replace(".", "/");
    runExample(exampleDir, clazz.getName(), options);
  }

  public static void runScriptExample(String prefix, String scriptName, boolean clustered) {
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    ExampleRunner.runExample(scriptDir, scriptDir + "/" + file.getName(), clustered);
  }

  public static void runScriptExample(String prefix, String scriptName, VertxOptions options) {
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    ExampleRunner.runExample(scriptDir, scriptDir + "/" + file.getName(), options);
  }

  public static void runExample(String exampleDir, String verticleID, boolean clustered) {
    runExample(exampleDir, verticleID, new VertxOptions().setClustered(clustered));
  }

  public static void runExample(String exampleDir, String verticleID, VertxOptions options) {
    System.setProperty("vertx.cwd", exampleDir);
    Consumer<Vertx> runner = vertx -> {
      try {
        vertx.deployVerticle(verticleID);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    };
    if (options.isClustered()) {
      Vertx.clusteredVertx(options, res -> {
        if (res.succeeded()) {
          Vertx vertx = res.result();
          runner.accept(vertx);
        } else {
          res.cause().printStackTrace();
        }
      });
    } else {
      Vertx vertx = Vertx.vertx(options);
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
