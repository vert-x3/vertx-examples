package io.vertx.example.core;

import io.vertx.codetrans.*;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConversionRunner {

  public static void main(String[] args) throws Exception {

    Lang lang;
    switch (System.getProperty("lang")) {
      case "groovy":
        lang = new GroovyLang();
        break;
      case "js":
        lang = new JavaScriptLang();
        break;
      default:
        throw new UnsupportedOperationException();
    }

    Map<String, Result> result = ConvertingProcessor.convert(ConversionRunner.class.getClassLoader(), lang,
      "echo/EchoServer.java",
      "echo/EchoClient.java",
      "http/Client.java",
      "http/Server.java",
      "https/Client.java",
      "https/Server.java",
      "proxy/Proxy.java",
      "proxy/Server.java",
      "proxy/Client.java",
      "eventbus_pointtopoint/Sender.java",
      "eventbus_pointtopoint/Receiver.java",
      "eventbus_pubsub/Sender.java",
      "eventbus_pubsub/Receiver.java",
      "eventbusbridge/BridgeServer.java",
      "sendfile/SendFile.java",
      "route_match/RouteMatchServer.java",
      "simpleform/SimpleFormServer.java",
      "simpleformupload/SimpleFormUploadServer.java",
      "sockjs/SockJSExample.java",
      "ssl/Server.java",
      "ssl/Client.java",
      "upload/Client.java",
      "upload/Server.java",
      "websockets/WebSocketsServer.java",
      "websockets/WebSocketsClient.java"
    );

    List<String> resources = Arrays.asList(
        "server-keystore.jks",
        "route_match/index.html",
        "eventbusbridge/index.html",
        "eventbusbridge/vertxbus.js",
        "sendfile/index.html",
        "sendfile/page1.html",
        "sendfile/page2.html",
        "simpleform/index.html",
        "simpleformupload/index.html",
        "sockjs/index.html",
        "upload/upload.txt",
        "websockets/ws.html"
    );
    for (String resource : resources) {
      InputStream in = ConversionRunner.class.getClassLoader().getResourceAsStream(resource);
      File dst = new File(resource).getAbsoluteFile();
      dst.getParentFile().mkdirs();
      Files.copy(in, dst.toPath());
    }

    for (Map.Entry<String, Result> generated : result.entrySet()) {
      File dst = new File(generated.getKey());
      dst.getParentFile().mkdirs();
      try (FileWriter writer = new FileWriter(dst)) {
        writer.append(generated.getValue().toString());
      }
    }

    //
    ClassLoader loader = new URLClassLoader(new URL[]{new File(".").getAbsoluteFile().toURI().toURL()}, Thread.currentThread().getContextClassLoader());
    Thread.currentThread().setContextClassLoader(loader);
    ConversionRunner.main(new String[0]);
  }
}
