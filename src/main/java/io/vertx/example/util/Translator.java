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

    Vertx vertx = Vertx.vertx();

    Lang lang = new JavaScriptLang();

    System.out.println("curr dir is " + (new File(".")).getAbsolutePath());

    String[] inputFiles = new String[] {
      "core-examples/src/main/java/io/vertx/example/core/net/echo/Server.java",
      "core-examples/src/main/java/io/vertx/example/core/net/echo/Client.java"
    };

    String[] outputFiles = new String[] {
      "core-examples/src/main/js/net/echo/server.js",
      "core-examples/src/main/js/net/echo/client.js",
    };

    for (int i = 0; i < inputFiles.length; i++) {
      String inputFile = inputFiles[i];
      String outputFile = outputFiles[i];

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
