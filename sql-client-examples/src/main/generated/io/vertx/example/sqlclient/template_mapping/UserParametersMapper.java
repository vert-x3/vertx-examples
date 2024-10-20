package io.vertx.example.sqlclient.template_mapping;

/**
 * Mapper for {@link User}.
 * NOTE: This class has been automatically generated from the {@link User} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface UserParametersMapper extends io.vertx.sqlclient.templates.TupleMapper<User> {

  UserParametersMapper INSTANCE = new UserParametersMapper() {};

  default io.vertx.sqlclient.Tuple map(java.util.function.Function<Integer, String> mapping, int size, User params) {
    java.util.Map<String, Object> args = map(params);
    Object[] array = new Object[size];
    for (int i = 0;i < array.length;i++) {
      String column = mapping.apply(i);
      array[i] = args.get(column);
    }
    return io.vertx.sqlclient.Tuple.wrap(array);
  }

  default java.util.Map<String, Object> map(User obj) {
    java.util.Map<String, Object> params = new java.util.HashMap<>();
    params.put("id", obj.getId());
    params.put("first_name", obj.getFirstName());
    params.put("last_name", obj.getLastName());
    return params;
  }
}
