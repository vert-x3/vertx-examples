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
  private static final String CORE_EXAMPLES_GROOVY_DIR = CORE_EXAMPLES_DIR + "/src/main/groovy/";
  private static final String CORE_EXAMPLES_KOTLIN_DIR = CORE_EXAMPLES_DIR + "/src/main/kotlin/";

  public static void runClusteredExample(Class clazz) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), null, true);
  }

  public static void runClusteredExample(Class clazz, VertxOptions options) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, options, null, true);
  }

  public static void runExample(Class clazz) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), null, false);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    runExample(CORE_EXAMPLES_JAVA_DIR, clazz, new VertxOptions(), options, false);
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    runScriptExample(CORE_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions(), false);
  }

  public static void runGroovyExampleClustered(String scriptName) {
    runScriptExample(CORE_EXAMPLES_GROOVY_DIR, scriptName, new VertxOptions(), true);
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

  // Kotlin

  public static void runKotlinExample(String scriptName) {
    runScriptExample(CORE_EXAMPLES_KOTLIN_DIR, scriptName, new VertxOptions(), false);
  }

  public static void runKotlinExampleClustered(String scriptName) {
    runScriptExample(CORE_EXAMPLES_KOTLIN_DIR, scriptName, new VertxOptions(), true);
  }

  static class KotlinHttpSimpleServerRunner {
    public static void main(String[] args) {
      Runner.runKotlinExample("io/vertx/example/core/http/simple/server.kt");
    }
  }

  static class KotlinHttpSimpleClientRunner {
    public static void main(String[] args) {
      Runner.runKotlinExample("io/vertx/example/core/http/simple/client.kt");
    }
  }

  public static void runExample(String exampleDir, Class clazz, VertxOptions options, DeploymentOptions deploymentOptions, boolean clustered) {
    runExample(exampleDir + clazz.getPackage().getName().replace(".", "/"), clazz.getName(), options, deploymentOptions, clustered);
  }


  public static void runScriptExample(String prefix, String scriptName, VertxOptions options, boolean clustered) {
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    runExample(scriptDir, scriptDir + "/" + file.getName(), options, null, clustered);
  }

  public static void runExample(String exampleDir, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions, boolean clustered) {
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
    if (clustered) {
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
