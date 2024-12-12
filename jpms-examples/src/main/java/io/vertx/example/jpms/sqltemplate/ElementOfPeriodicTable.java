package io.vertx.example.jpms.sqltemplate;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.RowMapped;
import io.vertx.sqlclient.templates.annotations.TemplateParameter;

@DataObject
@RowMapped
public class ElementOfPeriodicTable {

  private String element;
  private String symbol;

  @Column(name = "Element")
  public String getElement() {
    return element;
  }

  public void setElement(String element) {
    this.element = element;
  }

  @Column(name = "Symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
}
