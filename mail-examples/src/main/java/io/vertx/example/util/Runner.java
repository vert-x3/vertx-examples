package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Runner {

  private static final String WEB_EXAMPLES_DIR = "mail-examples";
  private static final String WEB_EXAMPLES_JAVA_DIR = WEB_EXAMPLES_DIR + "/src/main/java/";
  private static final String WEB_EXAMPLES_JS_DIR = WEB_EXAMPLES_DIR + "/src/main/js/";
  private static final String WEB_EXAMPLES_GROOVY_DIR = WEB_EXAMPLES_DIR + "/src/main/groovy/";
  private static final String WEB_EXAMPLES_RUBY_DIR = WEB_EXAMPLES_DIR + "/src/main/rb/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(WEB_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(WEB_EXAMPLES_JAVA_DIR, clazz, false);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    ExampleRunner.runJavaExample(WEB_EXAMPLES_JAVA_DIR, clazz, options);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_JS_DIR, scriptName, false);
  }

  public static void runJSExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_JS_DIR, scriptName, true);
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
    ExampleRunner.runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, true);
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
    ExampleRunner.runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, false);
  }

  public static void runRubyExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, true);
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
}
