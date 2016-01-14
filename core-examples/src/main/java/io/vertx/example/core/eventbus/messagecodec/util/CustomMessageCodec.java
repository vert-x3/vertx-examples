package io.vertx.example.core.eventbus.messagecodec.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Vert.x core example for {@link io.vertx.core.eventbus.EventBus} and {@link MessageCodec}
 * @author Junbong
 */
public class CustomMessageCodec implements MessageCodec<CustomMessage, CustomMessage> {
  @Override
  public void encodeToWire(Buffer buffer, CustomMessage customMessage) {
    // Easiest ways is using JSON object
    JsonObject jsonToEncode = new JsonObject();
    jsonToEncode.put("statusCode", customMessage.getStatusCode());
    jsonToEncode.put("resultCode", customMessage.getResultCode());
    jsonToEncode.put("summary", customMessage.getSummary());

    // Encode object to string
    String jsonToStr = jsonToEncode.encode();

    // Length of JSON: is NOT characters count
    int length = jsonToStr.getBytes().length;

    // Write data into given buffer
    buffer.appendInt(length);
    buffer.appendString(jsonToStr);
  }

  @Override
  public CustomMessage decodeFromWire(int position, Buffer buffer) {
    // My custom message starting from this *position* of buffer
    int _pos = position;

    // Length of JSON
    int length = buffer.getInt(_pos);

    // Get JSON string by it`s length
    // Jump 4 because getInt() == 4 bytes
    String jsonStr = buffer.getString(_pos+=4, _pos+=length);
    JsonObject contentJson = new JsonObject(jsonStr);

    // Get fields
    int statusCode = contentJson.getInteger("statusCode");
    String resultCode = contentJson.getString("resultCode");
    String summary = contentJson.getString("summary");

    // We can finally create custom message object
    return new CustomMessage(statusCode, resultCode, summary);
  }

  @Override
  public CustomMessage transform(CustomMessage customMessage) {
    // If a message is sent *locally* across the event bus.
    // This example sends message just as is
    return customMessage;
  }

  @Override
  public String name() {
    // Each codec must have a unique name.
    // This is used to identify a codec when sending a message and for unregistering codecs.
    return this.getClass().getSimpleName();
  }

  @Override
  public byte systemCodecID() {
    // Always -1
    return -1;
  }
}
