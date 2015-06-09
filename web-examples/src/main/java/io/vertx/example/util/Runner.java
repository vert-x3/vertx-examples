package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;

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
      Runner.runJSExample("io/vertx/example/web/templating/server.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_GROOVY_DIR, scriptName, true);
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
      Runner.runGroovyExample("io/vertx/example/web/templating/server.groovy");
    }
  }

  static class GroovyRestRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/web/rest/simple_rest.groovy");
    }
  }

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, false);
  }

  public static void runRubyExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(WEB_EXAMPLES_RUBY_DIR, scriptName, true);
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
      Runner.runRubyExample("io/vertx/example/web/templating/server.rb");
    }
  }
}
