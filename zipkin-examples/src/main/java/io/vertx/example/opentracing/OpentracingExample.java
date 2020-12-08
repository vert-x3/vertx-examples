package io.vertx.example.opentracing;

public class OpentracingExample {

  public static void main(String[] args) throws Exception {
    JokeService.main(args);
    HelloService.main(args);
    Gateway.main(args);
  }
}
