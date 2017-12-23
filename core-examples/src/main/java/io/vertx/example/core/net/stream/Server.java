package io.vertx.example.core.net.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 *  @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createNetServer().connectHandler(socket -> {

      // Create batch read stream
      BatchReadStream readStream = new BatchReadStream(socket);

      // Pause reading data (before pumping)
      readStream.pause();

      // Create batch write stream
      BatchWriteStream writeStream = new BatchWriteStream(socket);

      // Register read stream handler
      readStream.handler(batch -> {
        System.out.println("Server Received : " + batch.getRaw().toString());
        // Write back to the client
        writeStream.write(batch);
      });

      // Resume reading data
      readStream.resume();

    }).listen(1234);
    System.out.println("Batch server is now listening to port : 1234");
  }
}
