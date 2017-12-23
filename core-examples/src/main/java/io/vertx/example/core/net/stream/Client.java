package io.vertx.example.core.net.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.example.util.Runner;

import java.time.Instant;
import java.util.UUID;

/*
 *  @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createNetClient().connect(1234, "localhost", ar -> {
      if (ar.succeeded()) {

        NetSocket socket = ar.result();

        // Create batch read stream
        BatchReadStream readStream = new BatchReadStream(socket);

        // Pause reading data
        readStream.pause();

        // Register read stream handler
        readStream.handler(batch -> {
          System.out.println("Client Received : " + batch.getRaw().toString());
        });

        // Resume reading data
        readStream.resume();

        // Dummy JsonObject
        JsonObject jsonObject = new JsonObject()
          .put("id", UUID.randomUUID().toString())
          .put("name", "Vert.x")
          .put("timestamp", Instant.now());

        // Dummy JsonArray
        JsonArray jsonArray = new JsonArray()
          .add(UUID.randomUUID().toString())
          .add("Vert.x")
          .add(Instant.now());

        // Buffer
        Buffer buffer = Buffer.buffer("Vert.x is awesome!");

        // Create batch write stream
        BatchWriteStream writeStream = new BatchWriteStream(socket);

        // Write to socket
        writeStream.write(new Batch(jsonObject));
        writeStream.write(new Batch(jsonArray));
        writeStream.write(new Batch(buffer));

      } else {
        System.out.println("Failed to connect " + ar.cause());
      }
    });
  }
}
