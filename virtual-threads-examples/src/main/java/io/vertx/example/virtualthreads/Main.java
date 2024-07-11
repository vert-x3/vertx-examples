package io.vertx.example.virtualthreads;

import java.util.concurrent.StructuredTaskScope;

public class Main {

  public static void main(String[] args) throws Exception {
   try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      StructuredTaskScope.Subtask<String> s1 = scope.fork(() -> {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          System.out.println("INTERRUPTED");
          throw e;
        }
        return "Julien";
      });
      StructuredTaskScope.Subtask<String> s2 = scope.fork(() -> {
        Thread.sleep(1000);
        return "Thomas";
      });
      StructuredTaskScope.Subtask<String> s3 = scope.fork(() -> {
        throw new RuntimeException("ERROR");
      });
      scope.join();
      System.out.println(s1.get() + " " + s2.get());
    }
  }
}
