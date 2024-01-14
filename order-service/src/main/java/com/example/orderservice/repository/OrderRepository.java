package com.example.orderservice.repository;

import com.example.orderservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {

  Optional<Order> findByOrderNumber(Long id);
  Optional<Order> findFirstByOrderByIdDesc();
}
