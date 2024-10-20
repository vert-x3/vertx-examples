package io.vertx.example.sqlclient.template_mapping;

/**
 * Mapper for {@link User}.
 * NOTE: This class has been automatically generated from the {@link User} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface UserRowMapper extends io.vertx.sqlclient.templates.RowMapper<User> {

  UserRowMapper INSTANCE = new UserRowMapper() { };

  @io.vertx.codegen.annotations.GenIgnore
  java.util.stream.Collector<io.vertx.sqlclient.Row, ?, java.util.List<User>> COLLECTOR = java.util.stream.Collectors.mapping(INSTANCE::map, java.util.stream.Collectors.toList());

  @io.vertx.codegen.annotations.GenIgnore
  default User map(io.vertx.sqlclient.Row row) {
    User obj = new User();
    Object val;
    int idx;
    if ((idx = row.getColumnIndex("id")) != -1 && (val = row.getInteger(idx)) != null) {
      obj.setId((int)val);
    }
    if ((idx = row.getColumnIndex("first_name")) != -1 && (val = row.getString(idx)) != null) {
      obj.setFirstName((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("last_name")) != -1 && (val = row.getString(idx)) != null) {
      obj.setLastName((java.lang.String)val);
    }
    return obj;
  }
}
