package io.vertx.example;


import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {

    // This is basically the same example as the web-examples staticsite example but it's booted using
    // SpringBoot, not Vert.x
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Vertx vertx(){
      return Vertx.vertx();
  }

}
