package io.vertx.example.core.net.stream;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;

import java.util.Objects;

/*
 *  @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */

public class BatchStream implements ReadStream<Batch>, WriteStream<Batch> {

  private final RecordParser recordParser;
  private final WriteStream<Buffer> writeStream;
  private int size = -1;
  private Handler<Throwable> exceptionHandler;

  public BatchStream(ReadStream<Buffer> rs, WriteStream<Buffer> ws) {
    Objects.requireNonNull(rs, "ReadStream");
    Objects.requireNonNull(ws, "WriteStream");
    recordParser = RecordParser.newFixed(4, rs);
    writeStream = ws;
    // Propagate exceptions to the current stream
    recordParser.exceptionHandler(throwable -> {
      if (exceptionHandler != null) {
        exceptionHandler.handle(throwable);
      }
    });
    writeStream.exceptionHandler(throwable -> {
      if (exceptionHandler != null) {
        exceptionHandler.handle(throwable);
      }
    });
  }

  @Override
  public BatchStream exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public BatchStream write(Batch batch) {
    if (batch == null) {
      if (exceptionHandler != null) {
        exceptionHandler.handle(new NullPointerException());
      }
    } else {
      Buffer protocol = Buffer.buffer();
      protocol.appendInt(0);
      protocol.appendByte((byte) batch.getType());
      protocol.appendBuffer(batch.getRaw());
      protocol.setInt(0, protocol.length() - 4);
      writeStream.write(protocol);
    }
    return this;
  }

  @Override
  public void end() {
    writeStream.end();
  }

  @Override
  public BatchStream setWriteQueueMaxSize(int maxSize) {
    writeStream.setWriteQueueMaxSize(maxSize);
    return this;
  }

  @Override
  public boolean writeQueueFull() {
    return writeStream.writeQueueFull();
  }

  @Override
  public BatchStream drainHandler(Handler<Void> handler) {
    writeStream.drainHandler(handler);
    return this;
  }

  @Override
  public BatchStream handler(Handler<Batch> handler) {
    if (handler == null) {
      recordParser.handler(null);
      recordParser.exceptionHandler(null);
      recordParser.endHandler(null);
      return this;
    }
    recordParser.handler(buffer -> {
      try {
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
          final Buffer payload = buffer.getBuffer(1, buffer.length());
          switch (type) {
            // JsonObject
            case 'O': {
              handler.handle(new Batch(payload.toJsonObject()));
              break;
            }
            // JsonArray
            case 'A': {
              handler.handle(new Batch(payload.toJsonArray()));
              break;
            }
            // Buffer
            case 'B': {
              handler.handle(new Batch(payload));
              break;
            } // Invalid
            default: {
              if (exceptionHandler != null) {
                exceptionHandler.handle(new IllegalStateException("Invalid message " + type));
              }
            }
          }
        }
      } catch (Throwable throwable) {
        if (exceptionHandler != null) {
          exceptionHandler.handle(throwable);
        }
      }
    });
    return this;
  }

  @Override
  public BatchStream pause() {
    recordParser.pause();
    return this;
  }

  @Override
  public BatchStream resume() {
    recordParser.resume();
    return this;
  }

  @Override
  public BatchStream endHandler(Handler<Void> endHandler) {
    recordParser.endHandler(endHandler);
    return this;
  }
}