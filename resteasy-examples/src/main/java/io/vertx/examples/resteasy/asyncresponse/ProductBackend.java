package io.vertx.examples.resteasy.asyncresponse;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ProductBackend extends AbstractVerticle {

  private Map<String, JsonObject> products = new HashMap<>();

  public ProductBackend() {

    // Setup initial data
    addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
    addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
    addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
  }

  @Override
  public void start() throws Exception {

    // A simple backend
    vertx.eventBus().<JsonObject>consumer("backend", msg -> {

      JsonObject json = msg.body();

      switch (json.getString("op", "")) {
        case "get": {
          String productID = json.getString("id");
          msg.reply(products.get(productID));
          break;
        }
        case "add": {
          String productID = json.getString("id");
          JsonObject product = json.getJsonObject("product");
          product.put("id", productID);
          msg.reply(addProduct(product));
          break;
        }
        case "list": {
          JsonArray arr = new JsonArray();
          products.forEach((k, v) -> arr.add(v));
          msg.reply(arr);
          break;
        }
        default: {
          msg.fail(0, "operation not permitted");
        }
      }
    });
  }

  private boolean addProduct(JsonObject product) {
    if (product.containsKey("name") || product.containsKey("price") || product.containsKey("weight")) {
      products.put(product.getString("id"), product);
      return true;
    } else {
      return false;
    }
  }
}
