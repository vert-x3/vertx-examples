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

      // Pause reading data
      readStream.pause();

      // Create batch write stream
      BatchWriteStream writeStream = new BatchWriteStream(socket);

      // Register read stream handler
      readStream.handler(batch -> {

        // Print received batch object from the client
        System.out.println("Server Received : " + batch.getRaw().toString());

        // Write back batch object to the client
        writeStream.write(batch);

        // Check if write queue is full
        if (writeStream.writeQueueFull()) {

          // Pause reading data
          readStream.pause();

          // Called once write queue is ready to accept more data
          writeStream.drainHandler(done -> {

            // Resume reading data
            readStream.resume();

          });
        }
      }).endHandler(v -> socket.close())
        .exceptionHandler(t -> {
          t.printStackTrace();
          socket.close();
        });

      // Resume reading data
      readStream.resume();

    }).listen(1234);
    System.out.println("Batch server is now listening to port : 1234");
  }
}
