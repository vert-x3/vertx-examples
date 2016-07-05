package io.vertx.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * A configuration bean.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@Configuration
public class AppConfiguration {

  @Autowired
  Environment environment;

  int httpPort() {
    return environment.getProperty("http.port", Integer.class, 8080);
  }

}
