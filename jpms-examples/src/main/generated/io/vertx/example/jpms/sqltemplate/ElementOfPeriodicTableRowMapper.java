package io.vertx.example.jpms.sqltemplate;

/**
 * Mapper for {@link ElementOfPeriodicTable}.
 * NOTE: This class has been automatically generated from the {@link ElementOfPeriodicTable} original class using Vert.x codegen.
 */
@io.vertx.codegen.annotations.VertxGen
public interface ElementOfPeriodicTableRowMapper extends io.vertx.sqlclient.templates.RowMapper<ElementOfPeriodicTable> {

  ElementOfPeriodicTableRowMapper INSTANCE = new ElementOfPeriodicTableRowMapper() { };

  @io.vertx.codegen.annotations.GenIgnore
  java.util.stream.Collector<io.vertx.sqlclient.Row, ?, java.util.List<ElementOfPeriodicTable>> COLLECTOR = java.util.stream.Collectors.mapping(INSTANCE::map, java.util.stream.Collectors.toList());

  @io.vertx.codegen.annotations.GenIgnore
  default ElementOfPeriodicTable map(io.vertx.sqlclient.Row row) {
    ElementOfPeriodicTable obj = new ElementOfPeriodicTable();
    Object val;
    int idx;
    if ((idx = row.getColumnIndex("Element")) != -1 && (val = row.getString(idx)) != null) {
      obj.setElement((java.lang.String)val);
    }
    if ((idx = row.getColumnIndex("Symbol")) != -1 && (val = row.getString(idx)) != null) {
      obj.setSymbol((java.lang.String)val);
    }
    return obj;
  }
}
