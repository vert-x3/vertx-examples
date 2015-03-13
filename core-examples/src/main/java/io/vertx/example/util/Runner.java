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

  public static void runJSExampleClustered(String scriptName) {
    ExampleRunner.runJSExample(CORE_EXAMPLES_JS_DIR, scriptName, true);
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

  static class JSEchoSslServerRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/net/echossl/server.js");
    }
  }

  static class JSEchoSslClientRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/net/echossl/client.js");
    }
  }

  static class JSEventBusPointToPointReceiverRunner {
    public static void main(String[] args) {
      Runner.runJSExampleClustered("io/vertx/example/core/eventbus/pointtopoint/receiver.js");
    }
  }

  static class JSEventBusPointToPointSenderRunner {
    public static void main(String[] args) {
      Runner.runJSExampleClustered("io/vertx/example/core/eventbus/pointtopoint/sender.js");
    }
  }

  static class JSEventBusPubSubReceiverRunner {
    public static void main(String[] args) {
      Runner.runJSExampleClustered("io/vertx/example/core/eventbus/pubsub/receiver.js");
    }
  }

  static class JSEventBusPubSubSenderRunner {
    public static void main(String[] args) {
      Runner.runJSExampleClustered("io/vertx/example/core/eventbus/pubsub/sender.js");
    }
  }
}
