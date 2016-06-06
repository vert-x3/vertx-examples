package io.vertx.example.jgroups;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class NetworkUtils {

  /**
   * Picks the IP from localhost.
   * @return the IP, "127.0.0.1" if not found.
   */
  public static String getInterface() {
    try {
      return Inet4Address.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return "127.0.0.1";
    }
  }

}
