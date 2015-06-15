package io.vertx.examples.spring;

import io.vertx.core.Vertx;
import io.vertx.examples.spring.context.ExampleSpringConfiguration;
import io.vertx.examples.spring.verticle.ServerVerticle;
import io.vertx.examples.spring.verticle.SpringDemoVerticle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Runner for the vertx-spring sample
 *
 */
public class SpringExampleRunner {

  public static void main( String[] args ) {
    ApplicationContext context = new AnnotationConfigApplicationContext(ExampleSpringConfiguration.class);
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new SpringDemoVerticle(context));
    vertx.deployVerticle(new ServerVerticle());
  }

}
