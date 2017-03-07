package io.vertx.examples.service.groovy.internal;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Internal API.
 */
public class ConversionHelper {

  @SuppressWarnings("unchecked")
  public static Object unwrap(Object obj) {
    if (obj instanceof Map) {
      return toJsonObject((Map<String, Object>) obj);
    } else if (obj instanceof List) {
      return toJsonArray((List<Object>) obj);
    } else if (obj instanceof CharSequence) {
      return obj.toString();
    }
    return obj;
  }

  @SuppressWarnings("unchecked")
  private static Object toJsonElement(Object obj) {
    if (obj instanceof Map) {
      return toJsonObject((Map<String, Object>) obj);
    } else if (obj instanceof List) {
      return toJsonArray((List<Object>) obj);
    } else if (obj instanceof CharSequence) {
      return obj.toString();
    } else if (obj instanceof Buffer) {
      return Base64.getEncoder().encodeToString(((Buffer)obj).getBytes());
    }
    return obj;
  }

  public static JsonObject toJsonObject(Map<String, Object> map) {
    if (map == null) {
      return null;
    }
    map = new LinkedHashMap<>(map);
    map.entrySet().forEach(e -> e.setValue(toJsonElement(e.getValue())));
    return new JsonObject(map);
  }

  public static JsonArray toJsonArray(List<Object> list) {
    if (list == null) {
      return null;
    }
    list = new ArrayList<>(list);
    for (int i = 0;i < list.size();i++) {
      list.set(i, toJsonElement(list.get(i)));
    }
    return new JsonArray(list);
  }

  public static <T, R> R applyIfNotNull(T expr, Function<T, R> function) {
    if (expr != null) {
      return function.apply(expr);
    } else {
      return null;
    }
  }

  public static <T, R> R wrap(T t, Function<T, Object> f) {
    if (t != null) {
      return wrap(f.apply(t));
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T wrap(Object obj) {
    if (obj instanceof JsonObject) {
      return (T)fromJsonObject((JsonObject)obj);
    } else if (obj instanceof JsonArray) {
      return (T)fromJsonArray((JsonArray)obj);
    }
    return (T)obj;
  }

  public static Map<String, Object> fromJsonObject(JsonObject json) {
    if (json == null) {
      return null;
    }
    Map<String, Object> map = new LinkedHashMap<>(json.getMap());
    map.entrySet().forEach(entry -> {
      entry.setValue(wrap(entry.getValue()));
    });
    return map;
  }

  public static List<Object> fromJsonArray(JsonArray json) {
    if (json == null) {
      return null;
    }
    List<Object> list = new ArrayList<>(json.getList());
    for (int i = 0;i < list.size();i++) {
      list.set(i, wrap(list.get(i)));
    }
    return list;
  }
}
