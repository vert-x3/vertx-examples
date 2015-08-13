package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String SYNC_EXAMPLES_DIR = "sync-examples";
  private static final String SYNC_EXAMPLES_JAVA_DIR = SYNC_EXAMPLES_DIR + "/src/main/java/";
  private static final String SYNC_EXAMPLES_JS_DIR = SYNC_EXAMPLES_DIR + "/src/main/js/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(SYNC_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(SYNC_EXAMPLES_JAVA_DIR, clazz, false);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    ExampleRunner.runJavaExample(SYNC_EXAMPLES_JAVA_DIR, clazz, options);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    ExampleRunner.runScriptExample(SYNC_EXAMPLES_JS_DIR, scriptName, false);
  }

  public static void runJSExampleClustered(String scriptName) {
    ExampleRunner.runScriptExample(SYNC_EXAMPLES_JS_DIR, scriptName, true);
  }

}
