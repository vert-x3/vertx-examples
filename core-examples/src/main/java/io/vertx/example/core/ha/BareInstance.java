package io.vertx.example.core.ha;

import io.vertx.core.Launcher;

/**
 * Just start a bare instance of vert.x .
 * It will receive the Server verticle when the process is killed.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class BareInstance {

  public static void main(String[] args) {
    Launcher.main(new String[]{"bare"});
  }
}
