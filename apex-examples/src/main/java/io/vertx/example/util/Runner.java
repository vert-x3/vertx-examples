package io.vertx.example.util;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String APEX_EXAMPLES_DIR = "apex-examples";
  private static final String APEX_EXAMPLES_JAVA_DIR = APEX_EXAMPLES_DIR + "/src/main/java/";
  private static final String APEX_EXAMPLES_JS_DIR = APEX_EXAMPLES_DIR + "/src/main/js/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(APEX_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(APEX_EXAMPLES_JAVA_DIR, clazz, false);
  }


}
