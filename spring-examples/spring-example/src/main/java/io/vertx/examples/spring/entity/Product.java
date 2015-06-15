package io.vertx.examples.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Trivial JPA entity for vertx-spring demo
 */
@Entity
@Table(name="PRODUCT")
public class Product {

  @Id
  @Column(name="ID")
  private Integer productId;

  @Column
  private String description;

  public Integer getProductId() {
    return productId;
  }

  public String getDescription() {
    return description;
  }
}
