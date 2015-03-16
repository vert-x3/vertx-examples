package io.vertx.example.util;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String APEX_EXAMPLES_DIR = "apex-examples";
  private static final String APEX_EXAMPLES_JAVA_DIR = APEX_EXAMPLES_DIR + "/src/main/java/";
  private static final String APEX_EXAMPLES_JS_DIR = APEX_EXAMPLES_DIR + "/src/main/js/";
  private static final String APEX_EXAMPLES_GROOVY_DIR = APEX_EXAMPLES_DIR + "/src/main/groovy/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(APEX_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(APEX_EXAMPLES_JAVA_DIR, clazz, false);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    ExampleRunner.runJSExample(APEX_EXAMPLES_JS_DIR, scriptName, false);
  }

  public static void runJSExampleClustered(String scriptName) {
    ExampleRunner.runJSExample(APEX_EXAMPLES_JS_DIR, scriptName, true);
  }

  static class JSAuthRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/apex/auth/server.js");
    }
  }

  static class JSHelloWorldRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/apex/helloworld/server.js");
    }
  }

  static class JSRealtimeRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/apex/realtime/server.js");
    }
  }

  static class JSSessionsRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/apex/sessions/server.js");
    }
  }

  static class JSTemplatingRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/apex/templating/server.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    ExampleRunner.runJSExample(APEX_EXAMPLES_GROOVY_DIR, scriptName, false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    ExampleRunner.runJSExample(APEX_EXAMPLES_GROOVY_DIR, scriptName, true);
  }

  static class GroovyAuthRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/apex/auth/server.groovy");
    }
  }

  static class GroovyHelloWorldRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/apex/helloworld/server.groovy");
    }
  }

  static class GroovyRealtimeRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/apex/realtime/server.groovy");
    }
  }

  static class GroovySessionsRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/apex/sessions/server.groovy");
    }
  }

  static class GroovyTemplatingRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/apex/templating/server.groovy");
    }
  }
}
