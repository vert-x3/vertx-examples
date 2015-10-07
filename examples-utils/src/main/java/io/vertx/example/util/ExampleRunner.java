package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.io.IOException;
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

  public static void runJavaExample(String prefix, Class clazz, DeploymentOptions deploymentOptions) {
    String exampleDir = prefix + clazz.getPackage().getName().replace(".", "/");
    runExample(exampleDir, clazz.getName(), new VertxOptions(), deploymentOptions);
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
    runExample(exampleDir, verticleID, options, null);
  }

  public static void runExample(String exampleDir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
    // Smart cwd detection

    // Based on the current directory (.) and the desired directory (exampleDir), we try to compute the vertx.cwd
    // directory:
    try {
      // We need to use the canonical file. Without the file name is .
      File current = new File(".").getCanonicalFile();
      if (exampleDir.startsWith(current.getName())  && ! exampleDir.equals(current.getName())) {
        exampleDir = exampleDir.substring(current.getName().length() + 1);
      }
    } catch (IOException e) {
      // Ignore it.
    }

    System.setProperty("vertx.cwd", exampleDir);
    Consumer<Vertx> runner = vertx -> {
      try {
        if (deploymentOptions != null) {
          vertx.deployVerticle(verticleID, deploymentOptions);
        } else {
          vertx.deployVerticle(verticleID);
        }
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

}
