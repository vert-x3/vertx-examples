package io.vertx.example.zipkin;

public class ZipkinExample {

  public static void main(String[] args) throws Exception {
    JokeService.main(args);
    HelloService.main(args);
    Gateway.main(args);
  }
}
