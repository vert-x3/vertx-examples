package io.vertx.example.util;

import io.vertx.core.Vertx;

import java.io.File;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ExampleRunner {


  public static void runJavaExample(String prefix, Class clazz) {
    String exampleDir = prefix + clazz.getPackage().getName().replace(".", "/");
    runExample(exampleDir, clazz.getName());
  }

  public static void runJSExample(String prefix, String scriptName) {
    //String coreJSPrefix = "core-examples/src/main/js/";
    File file = new File(scriptName);
    String dirPart = file.getParent();
    String scriptDir = prefix + dirPart;
    System.out.println("scriptDir is " + scriptDir);
    ExampleRunner.runExample(scriptDir, scriptDir + "/" + file.getName());
  }

  public static void runExample(String exampleDir, String verticleID) {
    System.setProperty("vertx.cwd", exampleDir);
    try {
      Vertx.vertx().deployVerticle(verticleID);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

//  static class EchoServerRunner {
//    public static void main(String[] args) {
//      run(Server.class);
//    }
//  }
//
//  static class EchoClientRunner {
//    public static void main(String[] args) {
//      run(Client.class);
//    }
//  }
//
//  static class SendFileRunner {
//    public static void main(String[] args) {
//      run(SendFile.class);
//    }
//  }
//
//  // JS
//
//  static class EchoServerRunnerJS {
//    public static void main(String[] args) {
//      run("core-examples/src/main/js/echo/echo_server.js");
//    }
//  }
}
