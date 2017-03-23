package io.vertx.example.mqtt.server.util;

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

  private static final String MQTT_SERVER_EXAMPLES_DIR = "mqtt-server-examples";
  private static final String MQTT_SERVER_EXAMPLES_JAVA_DIR = MQTT_SERVER_EXAMPLES_DIR + "/src/main/java/";
  private static final String MQTT_SERVER_EXAMPLES_JS_DIR = MQTT_SERVER_EXAMPLES_DIR + "/src/main/js/";
  private static final String MQTT_SERVER_EXAMPLES_GROOVY_DIR = MQTT_SERVER_EXAMPLES_DIR + "/src/main/groovy/";
  private static final String MQTT_SERVER_EXAMPLES_RUBY_DIR = MQTT_SERVER_EXAMPLES_DIR + "/src/main/ruby/";

  public static void runExample(Class clazz) {
    runExample(MQTT_SERVER_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(false), null);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    runExample(MQTT_SERVER_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(false), options);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    runScriptExample(MQTT_SERVER_EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runJSExampleClustered(String scriptName) {
    runScriptExample(MQTT_SERVER_EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class JSSimpleRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/mqtt/server/simple/server.js");
    }
  }

  static class JSSslRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/mqtt/server/ssl/server.js");
    }
  }

  static class JSAppRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/mqtt/server/app/app.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(MQTT_SERVER_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runGroovyExampleClustered(String scriptName) {
    runScriptExample(MQTT_SERVER_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class GroovySimpleRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/mqtt/server/simple/server.groovy");
    }
  }

  static class GroovySslRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/mqtt/server/ssl/server.groovy");
    }
  }

  static class GroovyAppRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/mqtt/server/app/app.groovy");
    }
  }

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    runScriptExample(MQTT_SERVER_EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runRubyExampleClustered(String scriptName) {
    runScriptExample(MQTT_SERVER_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class RubySimpleRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/mqtt/server/simple/server.rb");
    }
  }

  static class RubySslRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/mqtt/server/ssl/server.rb");
    }
  }

  static class RubyAppRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/mqtt/server/app/app.rb");
    }
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
