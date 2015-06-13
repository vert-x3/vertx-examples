package io.vertx.examples.spring.service;

import io.vertx.examples.spring.entity.Product;
import io.vertx.examples.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Simple Spring service bean to expose the results of a trivial database call
 */
@Service
public class ProductService {

  @Autowired
  private ProductRepository repo;

  public List<Product> getAllProducts() {
    return repo.findAll();
  }

}
