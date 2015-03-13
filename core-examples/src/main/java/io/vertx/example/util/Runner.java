package io.vertx.example.util;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String CORE_EXAMPLES_DIR = "core-examples";
  private static final String CORE_EXAMPLES_JAVA_DIR = CORE_EXAMPLES_DIR + "/src/main/java/";
  private static final String CORE_EXAMPLES_JS_DIR = CORE_EXAMPLES_DIR + "/src/main/js/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(CORE_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(CORE_EXAMPLES_JAVA_DIR, clazz, false);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    ExampleRunner.runJSExample(CORE_EXAMPLES_JS_DIR, scriptName, false);
  }

  static class JSEchoServerRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/net/echo/server.js");
    }
  }

  static class JSEchoClientRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/net/echo/client.js");
    }
  }
}
