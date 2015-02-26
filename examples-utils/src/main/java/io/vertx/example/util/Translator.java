package io.vertx.example.util;

import io.vertx.codetrans.ConvertingProcessor;
import io.vertx.codetrans.JavaScriptLang;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Result;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import java.io.File;
import java.util.Map;

/**
 * FIXME All very hacky at the moment!
 *
 * Running this manually in the IDE to generate JS code
 */
public class Translator {

  public static void main(String[] args) throws Exception {
    translate("core-examples/src/main/java/io/vertx/example/core/", "core-examples/src/main/js/");
  }

  private static String toJSFile(String javaFile) {
    File f = new File(javaFile);
    String parentPart = f.getParent();
    String filePart = f.getName();
    String jsFile = parentPart + "/" + toUnderScores(filePart);
    return jsFile.replace(".java", ".js");
  }

  private static String toUnderScores(String str) {
    StringBuilder sb = new StringBuilder();
    boolean prevLowerCase = false;
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (Character.isUpperCase(ch)) {
        if (prevLowerCase) {
          sb.append("_");
        }
        sb.append(Character.toLowerCase(ch));
        prevLowerCase = false;
      } else {
        sb.append(ch);
        prevLowerCase = true;
      }
    }
    return sb.toString();
  }
  
  public static void translate(String prefix, String outPrefix) throws Exception {

    Vertx vertx = Vertx.vertx();

    Lang lang = new JavaScriptLang();

    System.out.println("curr dir is " + (new File(".")).getAbsolutePath());

    String[] inputFiles = new String[] {
      "net/echo/Server.java",
      "net/echo/Client.java",
      "net/echossl/Server.java",
      "net/echossl/Client.java",
      "http/https/Server.java",
      "http/https/Client.java",
      "http/simple/Server.java",
      "http/simple/Client.java",
      "http/proxy/Server.java",
      "http/proxy/Client.java",
      "http/proxy/Proxy.java",
      "http/sendfile/SendFile.java",
      "http/simpleform/SimpleFormServer.java",
      "http/simpleformupload/SimpleFormUploadServer.java",
      "http/upload/Server.java",
      "http/upload/Client.java",
      "http/websockets/Server.java",
      "http/websockets/Client.java",
      "verticle/deploy/DeployExample.java",
      "verticle/asyncstart/DeployExample.java",
      "execblocking/ExecBlockingExample.java",
      "eventbus/pointtopoint/Receiver.java",
      "eventbus/pointtopoint/Sender.java",
      "eventbus/pubsub/Receiver.java",
      "eventbus/pubsub/Sender.java"
    };

//    String[] outputFiles = new String[] {
//      "core-examples/src/main/js/net/echo/server.js",
//      "core-examples/src/main/js/net/echo/client.js",
//    };

    for (int i = 0; i < inputFiles.length; i++) {
      String inp = inputFiles[i];
      String inputFile = prefix + inp;
      System.out.println("input file is " + inputFile);

      //String outputFile = outputFiles[i];
      String out = toJSFile(inp);


      String outputFile = outPrefix + out;

      System.out.println("out is " + outputFile);


      Map<String, Result> result = ConvertingProcessor.convertFromFiles(Translator.class.getClassLoader(), lang, inputFile);

      Result res = result.values().iterator().next();
      if (res instanceof Result.Source) {
        Result.Source src = (Result.Source)res;
        File f =  new File(outputFile);
        f.getParentFile().mkdirs();
        vertx.fileSystem().writeFileBlocking(outputFile, Buffer.buffer(src.getValue()));
      } else {
        Result.Failure fail = (Result.Failure)res;
        fail.getCause().printStackTrace();
      }
    }

    vertx.close();

  }


//    List<String> resources = Arrays.asList(
//        "server-keystore.jks",
//        "route_match/index.html",
//        "eventbusbridge/index.html",
//        "eventbusbridge/vertxbus.js",
//        "sendfile/index.html",
//        "sendfile/page1.html",
//        "sendfile/page2.html",
//        "simpleform/index.html",
//        "simpleformupload/index.html",
//        "sockjs/index.html",
//        "upload/upload.txt",
//        "websockets/ws.html"
//    );
//    for (String resource : resources) {
//      InputStream in = ConversionRunner.class.getClassLoader().getResourceAsStream(resource);
//      File dst = new File(resource).getAbsoluteFile();
//      dst.getParentFile().mkdirs();
//      Files.copy(in, dst.toPath());
//    }
}
