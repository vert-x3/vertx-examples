package io.vertx.example.util;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String RX_EXAMPLES_DIR = "rx-examples";
  private static final String RX_EXAMPLES_JAVA_DIR = RX_EXAMPLES_DIR + "/src/main/java/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(RX_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(RX_EXAMPLES_JAVA_DIR, clazz, false);
  }
}
