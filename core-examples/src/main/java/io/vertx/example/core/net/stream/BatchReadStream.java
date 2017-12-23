package io.vertx.example.core.net.stream;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.core.streams.ReadStream;

/*
 *  @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */

public class BatchReadStream implements ReadStream<Batch> {

  final RecordParser recordParser;
  int size = -1;

  public BatchReadStream(ReadStream<Buffer> readStream) {
    this.recordParser = RecordParser.newFixed(4, readStream);
  }

  @Override
  public BatchReadStream exceptionHandler(Handler<Throwable> handler) {
    recordParser.exceptionHandler(handler);
    return this;
  }

  @Override
  public BatchReadStream handler(Handler<Batch> handler) {
    if (handler == null) {
      recordParser.handler(null);
      return this;
    }
    recordParser.handler(buffer -> {
      // Message size mode
      if (size == -1) {
        size = buffer.getInt(0);
        recordParser.fixedSizeMode(size);
        // Message body mode
      } else {
        size = -1;
        recordParser.fixedSizeMode(4);
        // Batch message type
        final char type = (char) buffer.getByte(0);
        // Batch message data (Buffer)
        final Buffer payLoad = buffer.getBuffer(1, buffer.length());
        switch (type) {
          // JsonObject
          case 'O': {
            handler.handle(new Batch(payLoad.toJsonObject()));
            break;
          }
          // JsonArray
          case 'A': {
            handler.handle(new Batch(payLoad.toJsonArray()));
            break;
          }
          // Buffer
          case 'B': {
            handler.handle(new Batch(payLoad));
            break;
          }
        }
      }
    });
    return this;
  }

  @Override
  public BatchReadStream pause() {
    recordParser.pause();
    return this;
  }

  @Override
  public BatchReadStream resume() {
    recordParser.resume();
    return this;
  }

  @Override
  public BatchReadStream endHandler(Handler<Void> endHandler) {
    recordParser.endHandler(endHandler);
    return this;
  }
}