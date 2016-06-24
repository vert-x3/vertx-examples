package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String CORE_EXAMPLES_DIR = "core-examples";
  private static final String CORE_EXAMPLES_JAVA_DIR = CORE_EXAMPLES_DIR + "/src/main/java/";
  private static final String CORE_EXAMPLES_JS_DIR = CORE_EXAMPLES_DIR + "/src/main/js/";
  private static final String CORE_EXAMPLES_GROOVY_DIR = CORE_EXAMPLES_DIR + "/src/main/groovy/";
  private static final String CORE_EXAMPLES_RUBY_DIR = CORE_EXAMPLES_DIR + "/src/main/rb/";

  public static void runClusteredExample(Class clazz) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(true), null);
  }

  public static void runClusteredExample(Class clazz, VertxOptions options) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, options.setClustered(true), null);
  }

  public static void runExample(Class clazz) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(false), null);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, new VertxOptions().setClustered(false), options);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    runScriptExample(CORE_EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runJSExampleClustered(String scriptName) {
    runScriptExample(CORE_EXAMPLES_JS_DIR, scriptName, new VertxOptions().setClustered(true));
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
      Runner.runJSExample("io/vertx/example/core/http/sendfile/send_file.js");
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
      Runner.runJSExample("io/vertx/example/core/http/simpleform/simple_form_server.js");
    }
  }

  static class JSHttpSimpleFormUploadRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/http/simpleformupload/simple_form_upload_server.js");
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
      Runner.runJSExample("io/vertx/example/core/verticle/asyncstart/deploy_example.js");
    }
  }

  static class JSVerticleDeployRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/core/verticle/deploy/deploy_example.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(CORE_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runGroovyExampleClustered(String scriptName) {
    runScriptExample(CORE_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(true));
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
      Runner.runGroovyExample("io/vertx/example/core/http/sendfile/send_file.groovy");
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
      Runner.runGroovyExample("io/vertx/example/core/http/simpleform/simple_form_server.groovy");
    }
  }

  static class GroovyHttpSimpleFormUploadRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/http/simpleformupload/simple_form_upload_server.groovy");
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
      Runner.runGroovyExample("io/vertx/example/core/verticle/asyncstart/deploy_example.groovy");
    }
  }

  static class GroovyVerticleDeployRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/verticle/deploy/deploy_example.groovy");
    }
  }

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    runScriptExample(CORE_EXAMPLES_RUBY_DIR, scriptName, new VertxOptions().setClustered(false));
  }

  public static void runRubyExampleClustered(String scriptName) {
    runScriptExample(CORE_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions().setClustered(true));
  }

  static class RubyEchoServerRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/net/echo/server.rb");
    }
  }

  static class RubyEchoClientRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/net/echo/client.rb");
    }
  }

  static class RubyEchoSslServerRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/net/echossl/server.rb");
    }
  }

  static class RubyEchoSslClientRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/net/echossl/client.rb");
    }
  }

  static class RubyEventBusPointToPointReceiverRunner {
    public static void main(String[] args) {
      Runner.runRubyExampleClustered("io/vertx/example/core/eventbus/pointtopoint/receiver.rb");
    }
  }

  static class RubyEventBusPointToPointSenderRunner {
    public static void main(String[] args) {
      Runner.runRubyExampleClustered("io/vertx/example/core/eventbus/pointtopoint/sender.rb");
    }
  }

  static class RubyEventBusPubSubReceiverRunner {
    public static void main(String[] args) {
      Runner.runRubyExampleClustered("io/vertx/example/core/eventbus/pubsub/receiver.rb");
    }
  }

  static class RubyEventBusPubSubSenderRunner {
    public static void main(String[] args) {
      Runner.runRubyExampleClustered("io/vertx/example/core/eventbus/pubsub/sender.rb");
    }
  }

  static class RubyHttpsServerRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/https/server.rb");
    }
  }

  static class RubyHttpsClientRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/https/client.rb");
    }
  }

  static class RubyHttpProxyServerRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/proxy/server.rb");
    }
  }

  static class RubyHttpProxyProxyRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/proxy/proxy.rb");
    }
  }

  static class RubyHttpProxyClientRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/proxy/client.rb");
    }
  }

  static class RubyHttpSendFileRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/sendfile/send_file.rb");
    }
  }

  static class RubyHttpSimpleServerRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/simple/server.rb");
    }
  }

  static class RubyHttpSimpleClientRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/simple/client.rb");
    }
  }

  static class RubyHttpSimpleFormRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/simpleform/simple_form_server.rb");
    }
  }

  static class RubyHttpSimpleFormUploadRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/simpleformupload/simple_form_upload_server.rb");
    }
  }

  static class RubyHttpUploadServerRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/upload/server.rb");
    }
  }

  static class RubyHttpUploadClientRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/upload/client.rb");
    }
  }

  static class RubyHttpWebsocketsServerRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/websockets/server.rb");
    }
  }

  static class RubyHttpWebsocketsClientRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/http/websockets/client.rb");
    }
  }

  static class RubyVerticleAsyncStartRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/core/verticle/asyncstart/deploy_example.rb");
    }
  }

  static class RubyVerticleDeployRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/core/verticle/deploy/deploy_example.rb");
    }
  }

  public static void runExample(String exampleDir, Class clazz, VertxOptions options, DeploymentOptions
      deploymentOptions) {
    runExample(exampleDir + clazz.getPackage().getName().replace(".", "/"), clazz.getName(), options, deploymentOptions);
  }


  public static void runScriptExample(String prefix, String scriptName, VertxOptions options) {
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    runExample(scriptDir, scriptDir + "/" + file.getName(), options, null);
  }

  public static void runExample(String exampleDir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
    if (options == null) {
      // Default parameter
      options = new VertxOptions();
    }
    // Smart cwd detection

    // Based on the current directory (.) and the desired directory (exampleDir), we try to compute the vertx.cwd
    // directory:
    try {
      // We need to use the canonical file. Without the file name is .
      File current = new File(".").getCanonicalFile();
      if (exampleDir.startsWith(current.getName())  && ! exampleDir.equals(current.getName())) {
        exampleDir = exampleDir.substring(current.getName().length() + 1);
      }
    } catch (IOException e) {
      // Ignore it.
    }

    System.setProperty("vertx.cwd", exampleDir);
    Consumer<Vertx> runner = vertx -> {
      try {
        if (deploymentOptions != null) {
          vertx.deployVerticle(verticleID, deploymentOptions);
        } else {
          vertx.deployVerticle(verticleID);
        }
      } catch (Throwable t) {
        t.printStackTrace();
      }
    };
    if (options.isClustered()) {
      Vertx.clusteredVertx(options, res -> {
        if (res.succeeded()) {
          Vertx vertx = res.result();
          runner.accept(vertx);
        } else {
          res.cause().printStackTrace();
        }
      });
    } else {
      Vertx vertx = Vertx.vertx(options);
      runner.accept(vertx);
    }
  }
}
