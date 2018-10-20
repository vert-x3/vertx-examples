package io.vertx.examples.webapiservice.models;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.asd.models.CreateTransactionRequestBody}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.asd.models.CreateTransactionRequestBody} original class using Vert.x codegen.
 */
 class CreateTransactionRequestBodyConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, CreateTransactionRequestBody obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "batch":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<Transaction> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new Transaction((JsonObject)item));
            });
            obj.setBatch(list);
          }
          break;
        case "batchName":
          if (member.getValue() instanceof String) {
            obj.setBatchName((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(CreateTransactionRequestBody obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(CreateTransactionRequestBody obj, java.util.Map<String, Object> json) {
    if (obj.getBatch() != null) {
      JsonArray array = new JsonArray();
      obj.getBatch().forEach(item -> array.add(item.toJson()));
      json.put("batch", array);
    }
    if (obj.getBatchName() != null) {
      json.put("batchName", obj.getBatchName());
    }
  }
}
