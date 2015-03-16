package io.vertx.example.unit.util;

import io.vertx.example.util.ExampleRunner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String UNIT_EXAMPLES_DIR = "unit-examples";
  private static final String UNIT_EXAMPLES_JAVA_DIR = UNIT_EXAMPLES_DIR + "/src/main/java/";
  private static final String UNIT_EXAMPLES_JS_DIR = UNIT_EXAMPLES_DIR + "/src/main/js/";
  private static final String UNIT_EXAMPLES_GROOVY_DIR = UNIT_EXAMPLES_DIR + "/src/main/groovy/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(UNIT_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(UNIT_EXAMPLES_JAVA_DIR, clazz, false);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    ExampleRunner.runJSExample(UNIT_EXAMPLES_JS_DIR, scriptName, false);
  }

  public static void runJSExampleClustered(String scriptName) {
    ExampleRunner.runJSExample(UNIT_EXAMPLES_JS_DIR, scriptName, true);
  }

  static class JSVertxUnitTest {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/unit/test/vertxunittest.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    ExampleRunner.runJSExample(UNIT_EXAMPLES_GROOVY_DIR, scriptName, false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    ExampleRunner.runJSExample(UNIT_EXAMPLES_GROOVY_DIR, scriptName, true);
  }

  static class GroovyVertxUnitTest {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/unit/test/vertxunittest.groovy");
    }
  }
}
