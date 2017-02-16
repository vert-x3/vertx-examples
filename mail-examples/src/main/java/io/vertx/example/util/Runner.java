package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Runner {

  private static final String WEB_EXAMPLES_DIR = "mail-examples";
  private static final String WEB_EXAMPLES_JAVA_DIR = WEB_EXAMPLES_DIR + "/src/main/java/";
  private static final String WEB_EXAMPLES_JS_DIR = WEB_EXAMPLES_DIR + "/src/main/js/";
  private static final String WEB_EXAMPLES_GROOVY_DIR = WEB_EXAMPLES_DIR + "/src/main/groovy/";
  private static final String WEB_EXAMPLES_RUBY_DIR = WEB_EXAMPLES_DIR + "/src/main/ruby/";

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

  static class JSMailEB {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/mail/mail_eb.js");
    }
  }

  static class JSMailHeaders {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/mail/mail_headers.js");
    }
  }

  static class JSMailLocalhost {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/mail/mail_localhost.js");
    }
  }

  static class JSMailLogin {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/mail/mail_login.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runGroovyExampleClustered(String scriptName) {
    runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class GroovyMailEB {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/mail/mail_eb.groovy");
    }
  }

  static class GroovyMailHeaders {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/mail/mail_headers.groovy");
    }
  }

  static class GroovyMailLocalhost {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/mail/mail_localhost.groovy");
    }
  }

  static class GroovyMailLogin {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/mail/mail_login.groovy");
    }
  }

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runRubyExampleClustered(String scriptName) {
    runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class RubyMailEB {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/mail/mail_eb.rb");
    }
  }

  static class RubyMailHeaders {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/mail/mail_headers.rb");
    }
  }

  static class RubyMailLocalhost {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/mail/mail_localhost.rb");
    }
  }

  static class RubyMailLogin {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/mail/mail_login.rb");
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
