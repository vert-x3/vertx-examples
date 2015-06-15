package io.vertx.examples.spring.repository;

import io.vertx.examples.spring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository to connect our service bean to data
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
