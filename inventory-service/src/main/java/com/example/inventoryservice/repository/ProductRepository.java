package com.example.inventoryservice.repository;

import com.example.inventoryservice.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

  Optional<Product> findByCode(String code);
}
