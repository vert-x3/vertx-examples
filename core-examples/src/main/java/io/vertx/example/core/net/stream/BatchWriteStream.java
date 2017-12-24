package io.vertx.example.core.net.stream;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;

/*
 *  @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */

public class BatchWriteStream implements WriteStream<Batch> {

  private final WriteStream<Buffer> writeStream;

  public BatchWriteStream(WriteStream<Buffer> writeStream) {
    this.writeStream = writeStream;
  }

  @Override
  public BatchWriteStream exceptionHandler(Handler<Throwable> handler) {
    writeStream.exceptionHandler(handler);
    return this;
  }

  @Override
  public BatchWriteStream write(Batch batch) {
    final Buffer protocol = Buffer.buffer();
    protocol.appendInt(0);
    protocol.appendByte((byte) batch.getType());
    protocol.appendBuffer(batch.getRaw());
    protocol.setInt(0, protocol.length() - 4);
    writeStream.write(protocol);
    return this;
  }

  @Override
  public void end() {
    writeStream.end();
  }

  @Override
  public BatchWriteStream setWriteQueueMaxSize(int maxSize) {
    writeStream.setWriteQueueMaxSize(maxSize);
    return this;
  }

  @Override
  public boolean writeQueueFull() {
    return writeStream.writeQueueFull();
  }

  @Override
  public BatchWriteStream drainHandler(Handler<Void> handler) {
    writeStream.drainHandler(handler);
    return this;
  }
}
