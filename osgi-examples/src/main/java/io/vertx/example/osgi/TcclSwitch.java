package io.vertx.example.osgi;

import java.util.concurrent.Callable;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class TcclSwitch {

  public static <T> T executeWithTCCLSwitch(Callable<T> action) throws Exception {
    final ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(TcclSwitch.class.getClassLoader());
      return action.call();
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

  public static void executeWithTCCLSwitch(Runnable action) {
    final ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(TcclSwitch.class.getClassLoader());
      action.run();
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

}
