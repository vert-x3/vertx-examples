package io.vertx.examples.utils;

import io.vertx.example.util.ExampleRunner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String MONGO_EXAMPLES_DIR = "mongo-examples";
  private static final String MONGO_EXAMPLES_JAVA_DIR = MONGO_EXAMPLES_DIR + "/src/main/java/";
  private static final String MONGO_EXAMPLES_JS_DIR = MONGO_EXAMPLES_DIR + "/src/main/js/";

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(MONGO_EXAMPLES_JAVA_DIR, clazz, false);
  }

  public static void runJSExample(String scriptName) {
    ExampleRunner.runScriptExample(MONGO_EXAMPLES_JS_DIR, "io/vertx/examples/mongo/mongo_client_verticle.js", false);
  }


}
