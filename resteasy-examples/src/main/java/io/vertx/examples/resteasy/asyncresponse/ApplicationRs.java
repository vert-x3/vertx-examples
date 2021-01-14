package io.vertx.examples.resteasy.asyncresponse;


import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * The Application of the JAX-RS interface
 *
 */
public class ApplicationRs extends Application {

  private Set<Object> singletons = new HashSet<>();
  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }


}
