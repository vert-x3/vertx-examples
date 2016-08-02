package io.vertx.example.camel.rmi;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HelloServiceImpl implements HelloService {
  @Override
  public String hello(String name) {
    return "Hello " + name;
  }
}
