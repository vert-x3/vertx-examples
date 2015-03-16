package io.vertx.example.util;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String CORE_EXAMPLES_DIR = "core-examples";
  private static final String CORE_EXAMPLES_JAVA_DIR = CORE_EXAMPLES_DIR + "/src/main/java/";
  private static final String CORE_EXAMPLES_JS_DIR = CORE_EXAMPLES_DIR + "/src/main/js/";
  private static final String CORE_EXAMPLES_GROOVY_DIR = CORE_EXAMPLES_DIR + "/src/main/groovy/";

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

  static class JSHttpsServerRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/https/server.js");
    }
  }

  static class JSHttpsClientRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/https/client.js");
    }
  }

  static class JSHttpProxyServerRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/proxy/server.js");
    }
  }

  static class JSHttpProxyProxyRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/proxy/proxy.js");
    }
  }

  static class JSHttpProxyClientRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/proxy/client.js");
    }
  }

  static class JSHttpSendFileRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/sendfile/sendfile.js");
    }
  }

  static class JSHttpSimpleServerRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/simple/server.js");
    }
  }

  static class JSHttpSimpleClientRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/simple/client.js");
    }
  }

  static class JSHttpSimpleFormRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/simpleform/simpleformserver.js");
    }
  }

  static class JSHttpSimpleFormUploadRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/simpleformupload/simpleformuploadserver.js");
    }
  }

  static class JSHttpUploadServerRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/upload/server.js");
    }
  }

  static class JSHttpUploadClientRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/upload/client.js");
    }
  }

  static class JSHttpWebsocketsServerRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/websockets/server.js");
    }
  }

  static class JSHttpWebsocketsClientRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/websockets/client.js");
    }
  }

  static class JSVerticleAsyncStartRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/verticle/asyncstart/deployexample.js");
    }
  }

  static class JSVerticleDeployRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/verticle/deploy/deployexample.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    ExampleRunner.runJSExample(CORE_EXAMPLES_GROOVY_DIR, scriptName, false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    ExampleRunner.runJSExample(CORE_EXAMPLES_GROOVY_DIR, scriptName, true);
  }

  static class GroovyEchoServerRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/net/echo/server.groovy");
    }
  }

  static class GroovyEchoClientRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/net/echo/client.groovy");
    }
  }

  static class GroovyEchoSslServerRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/net/echossl/server.groovy");
    }
  }

  static class GroovyEchoSslClientRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/net/echossl/client.groovy");
    }
  }

  static class GroovyEventBusPointToPointReceiverRunner {
    public static void main(String[] args) {
      Runner.runGroovyExampleClustered("io/vertx/example/core/eventbus/pointtopoint/receiver.groovy");
    }
  }

  static class GroovyEventBusPointToPointSenderRunner {
    public static void main(String[] args) {
      Runner.runGroovyExampleClustered("io/vertx/example/core/eventbus/pointtopoint/sender.groovy");
    }
  }

  static class GroovyEventBusPubSubReceiverRunner {
    public static void main(String[] args) {
      Runner.runGroovyExampleClustered("io/vertx/example/core/eventbus/pubsub/receiver.groovy");
    }
  }

  static class GroovyEventBusPubSubSenderRunner {
    public static void main(String[] args) {
      Runner.runGroovyExampleClustered("io/vertx/example/core/eventbus/pubsub/sender.groovy");
    }
  }

  static class GroovyHttpsServerRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/https/server.groovy");
    }
  }

  static class GroovyHttpsClientRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/https/client.groovy");
    }
  }

  static class GroovyHttpProxyServerRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/proxy/server.groovy");
    }
  }

  static class GroovyHttpProxyProxyRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/proxy/proxy.groovy");
    }
  }

  static class GroovyHttpProxyClientRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/proxy/client.groovy");
    }
  }

  static class GroovyHttpSendFileRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/sendfile/sendfile.groovy");
    }
  }

  static class GroovyHttpSimpleServerRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/simple/server.groovy");
    }
  }

  static class GroovyHttpSimpleClientRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/simple/client.groovy");
    }
  }

  static class GroovyHttpSimpleFormRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/simpleform/simpleformserver.groovy");
    }
  }

  static class GroovyHttpSimpleFormUploadRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/simpleformupload/simpleformuploadserver.groovy");
    }
  }

  static class GroovyHttpUploadServerRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/upload/server.groovy");
    }
  }

  static class GroovyHttpUploadClientRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/upload/client.groovy");
    }
  }

  static class GroovyHttpWebsocketsServerRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/websockets/server.groovy");
    }
  }

  static class GroovyHttpWebsocketsClientRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/websockets/client.groovy");
    }
  }

  static class GroovyVerticleAsyncStartRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/verticle/asyncstart/deployexample.groovy");
    }
  }

  static class GroovyVerticleDeployRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/verticle/deploy/deployexample.groovy");
    }
  }
}
