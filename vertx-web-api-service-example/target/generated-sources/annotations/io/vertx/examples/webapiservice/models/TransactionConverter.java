package io.vertx.examples.webapiservice.models;

import io.vertx.core.json.JsonObject;

/**
 * Converter for {@link Transaction}.
 * NOTE: This class has been automatically generated from the {@link Transaction} original class using Vert.x codegen.
 */
 class TransactionConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Transaction obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "from":
          if (member.getValue() instanceof String) {
            obj.setFrom((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "message":
          if (member.getValue() instanceof String) {
            obj.setMessage((String)member.getValue());
          }
          break;
        case "to":
          if (member.getValue() instanceof String) {
            obj.setTo((String)member.getValue());
          }
          break;
        case "value":
          if (member.getValue() instanceof Number) {
            obj.setValue(((Number)member.getValue()).doubleValue());
          }
          break;
      }
    }
  }

   static void toJson(Transaction obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(Transaction obj, java.util.Map<String, Object> json) {
    if (obj.getFrom() != null) {
      json.put("from", obj.getFrom());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getMessage() != null) {
      json.put("message", obj.getMessage());
    }
    if (obj.getTo() != null) {
      json.put("to", obj.getTo());
    }
    if (obj.getValue() != null) {
      json.put("value", obj.getValue());
    }
  }
}
