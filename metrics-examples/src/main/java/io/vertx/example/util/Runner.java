package io.vertx.example.util;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String METRICS_EXAMPLES_DIR = "apex-examples";
  private static final String METRICS_EXAMPLES_JAVA_DIR = METRICS_EXAMPLES_DIR + "/src/main/java/";
//  private static final String METRICS_EXAMPLES_JS_DIR = METRICS_EXAMPLES_DIR + "/src/main/js/";
//  private static final String METRICS_EXAMPLES_GROOVY_DIR = METRICS_EXAMPLES_DIR + "/src/main/groovy/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(METRICS_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(METRICS_EXAMPLES_JAVA_DIR, clazz, false);
  }

}
