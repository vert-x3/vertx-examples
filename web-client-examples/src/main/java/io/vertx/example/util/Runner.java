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
public class Runner {

  private static final String WEB_CLIENT_EXAMPLES_DIR = "web-client-examples";
  private static final String WEB_CLIENT_EXAMPLES_JAVA_DIR = WEB_CLIENT_EXAMPLES_DIR + "/src/main/java/";
  private static final String WEB_CLIENT_EXAMPLES_GROOVY_DIR = WEB_CLIENT_EXAMPLES_DIR + "/src/main/groovy/";

  public static void runExample(Class clazz) {
    runExample(WEB_CLIENT_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), null);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    runExample(WEB_CLIENT_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), options);
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(WEB_CLIENT_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions());
  }

  public static void runExample(String exampleDir, Class clazz, VertxOptions options, DeploymentOptions
    deploymentOptions) {
    runExample(exampleDir + clazz.getPackage().getName().replace(".", "/"), clazz.getName(), options, deploymentOptions);
  }


  public static void runScriptExample(String prefix, String scriptName, VertxOptions options) {
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    runExample(scriptDir, scriptDir + "/" + file.getName(), options, null);
  }

  public static void runExample(String exampleDir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
    if (options == null) {
      // Default parameter
      options = new VertxOptions();
    }
    // Smart cwd detection

    // Based on the current directory (.) and the desired directory (exampleDir), we try to compute the vertx.cwd
    // directory:
    try {
      // We need to use the canonical file. Without the file name is .
      File current = new File(".").getCanonicalFile();
      if (exampleDir.startsWith(current.getName()) && !exampleDir.equals(current.getName())) {
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
    Vertx vertx = Vertx.vertx(options);
    runner.accept(vertx);
  }
}
