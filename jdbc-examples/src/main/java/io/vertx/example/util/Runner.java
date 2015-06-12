package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Runner {

  private static final String EXAMPLES_DIR = "jdbc-examples";
  private static final String EXAMPLES_JAVA_DIR = EXAMPLES_DIR + "/src/main/java/";
  private static final String EXAMPLES_JS_DIR = EXAMPLES_DIR + "/src/main/js/";
  private static final String EXAMPLES_GROOVY_DIR = EXAMPLES_DIR + "/src/main/groovy/";
  private static final String EXAMPLES_RUBY_DIR = EXAMPLES_DIR + "/src/main/rb/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(EXAMPLES_JAVA_DIR, clazz, false);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    ExampleRunner.runJavaExample(EXAMPLES_JAVA_DIR, clazz, options);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    ExampleRunner.runScriptExample(EXAMPLES_JS_DIR, scriptName, false);
  }

  public static void runJSExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(EXAMPLES_JS_DIR, scriptName, true);
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    ExampleRunner.runScriptExample(EXAMPLES_GROOVY_DIR, scriptName, false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(EXAMPLES_GROOVY_DIR, scriptName, true);
  }

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    ExampleRunner.runScriptExample(EXAMPLES_RUBY_DIR, scriptName, false);
  }

  public static void runRubyExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(EXAMPLES_RUBY_DIR, scriptName, true);
  }
}
