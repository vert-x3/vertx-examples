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

  private static final String WEB_EXAMPLES_DIR = "web-examples";
  private static final String WEB_EXAMPLES_JAVA_DIR = WEB_EXAMPLES_DIR + "/src/main/java/";
  private static final String WEB_EXAMPLES_GROOVY_DIR = WEB_EXAMPLES_DIR + "/src/main/groovy/";

  public static void runClusteredExample(Class clazz) {
    runExample(WEB_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), null, true);
  }

  public static void runExample(Class clazz) {
    runExample(WEB_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), null, false);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    runExample(WEB_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), options, false);
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions(), false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions(), true);
  }

  static class GroovyAuthRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/auth/server.groovy");
    }
  }

  static class GroovyAuthJDBC {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/authjdbc/server.groovy");
    }
  }

  static class GroovyHelloWorldRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/helloworld/server.groovy");
    }
  }

  static class GroovyChatRunner {
    public static void main(String[] args) { Runner.runGroovyExample("io/vertx/example/web/chat/server.groovy"); }
  }

  static class GroovyRealtimeRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/realtime/server.groovy");
    }
  }

  static class GroovySessionsRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/sessions/server.groovy");
    }
  }

  static class GroovyTemplatingRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/templating/mvel/server.groovy");
    }
  }

  static class GroovyRestRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/rest/simple_rest.groovy");
    }
  }

  public static void runExample(String exampleDir, Class clazz, VertxOptions options, DeploymentOptions
    deploymentOptions, boolean clustered) {
    runExample(exampleDir + clazz.getPackage().getName().replace(".", "/"), clazz.getName(), options, deploymentOptions, clustered);
  }


  public static void runScriptExample(String prefix, String scriptName, VertxOptions options, boolean clustered) {
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    runExample(scriptDir, scriptDir + "/" + file.getName(), options, null, clustered);
  }

  public static void runExample(String exampleDir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions, boolean clustered) {
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
    if (clustered) {
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
