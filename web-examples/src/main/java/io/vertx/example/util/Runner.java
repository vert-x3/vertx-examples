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
  private static final String WEB_EXAMPLES_JS_DIR = WEB_EXAMPLES_DIR + "/src/main/js/";
  private static final String WEB_EXAMPLES_GROOVY_DIR = WEB_EXAMPLES_DIR + "/src/main/groovy/";
  private static final String WEB_EXAMPLES_RUBY_DIR = WEB_EXAMPLES_DIR + "/src/main/rb/";

  public static void runClusteredExample(Class clazz) {
    runExample(WEB_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(true), null);
  }

  public static void runExample(Class clazz) {
    runExample(WEB_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(false), null);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    runExample(WEB_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(false), options);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    runScriptExample(WEB_EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runJSExampleClustered(String scriptName) {
    runScriptExample(WEB_EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class JSAuthRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/web/auth/server.js");
    }
  }

  static class JSAuthJDBC {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/web/authjdbc/server.js");
    }
  }

  static class JSHelloWorldRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/web/helloworld/server.js");
    }
  }

  static class JSRealtimeRunner {
    public static void main(String[] args) { Runner.runJSExample("io/vertx/example/web/realtime/server.js"); }
  }

  static class JSChatRunner {
    public static void main(String[] args) {
            Runner.runJSExample("io/vertx/example/web/chat/server.js");
        }
  }

  static class JSSessionsRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/web/sessions/server.js");
    }
  }

  static class JSTemplatingRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/web/templating/mvel/server.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runGroovyExampleClustered(String scriptName) {
    runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(true));
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

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runRubyExampleClustered(String scriptName) {
    runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class RubyAuthRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/web/auth/server.rb");
    }
  }

  static class RubyAuthJDBC {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/web/authjdbc/server.rb");
    }
  }
  static class RubyHelloWorldRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/web/helloworld/server.rb");
    }
  }

  static class RubyChatRunner {
    public static void main(String[] args) { Runner.runRubyExample("io/vertx/example/web/chat/server.rb");
    }
  }

  static class RubyRealtimeRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/web/realtime/server.rb");
    }
  }

  static class RubySessionsRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/web/sessions/server.rb");
    }
  }

  static class RubyTemplatingRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/web/templating/mvel/server.rb");
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
