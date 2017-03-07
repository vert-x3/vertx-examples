package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Runner {

  private static final String EXAMPLES_DIR = "grpc-examples";
  private static final String EXAMPLES_JAVA_DIR = EXAMPLES_DIR + "/src/main/java/";
  private static final String EXAMPLES_JS_DIR = EXAMPLES_DIR + "/src/main/js/";
  private static final String EXAMPLES_GROOVY_DIR = EXAMPLES_DIR + "/src/main/groovy/";
  private static final String EXAMPLES_RUBY_DIR = EXAMPLES_DIR + "/src/main/ruby/";

  public static void runClusteredExample(Class clazz) {
    runExample(EXAMPLES_JAVA_DIR, clazz,
        new VertxOptions().setClustered(true), null);
  }

  public static void runExample(Class clazz) {
    runExample(EXAMPLES_JAVA_DIR, clazz,
        new VertxOptions().setClustered(false), null);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    runExample(EXAMPLES_JAVA_DIR, clazz,
        new VertxOptions().setClustered(false), options);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    runScriptExample(EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runJSExampleClustered(String scriptName) {
    runScriptExample(EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runGroovyExampleClustered(String scriptName) {
    runScriptExample(EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    runScriptExample(EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runRubyExampleClustered(String scriptName) {
    runScriptExample(EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(true));
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
