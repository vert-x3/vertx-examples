package io.vertx.example.core.echo;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.streams.Pump;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class EchoServer extends AbstractVerticle {

  @Override
  @CodeTranslate
  public void start() throws Exception {

    vertx.createNetServer().connectHandler(socket -> {

      // Create a pump
      Pump.pump(socket, socket).start();

    }).listen(1234);

    System.out.println("Echo server is now listening");

  }
}
