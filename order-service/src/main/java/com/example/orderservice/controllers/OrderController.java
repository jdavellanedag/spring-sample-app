package com.example.orderservice.controllers;

import com.example.orderservice.models.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService service;

  @GetMapping
  public ResponseEntity<List<Order>> getAll() {
    return ResponseEntity.ok(service.getOrders());
  }

  @GetMapping("/{orderNumber}")
  public ResponseEntity<Order> getOrder(@PathVariable Long orderNumber) {
    return ResponseEntity.ok(service.getOrderById(orderNumber));
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody Order order){
    return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(order));
  }

  @PutMapping("/{orderNumber}")
  public ResponseEntity<Void> updateOrder(@PathVariable Long orderNumber, @RequestBody Order order){
    service.updateOrder(orderNumber, order);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{orderNumber}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long orderNumber){
    service.deleteOrder(orderNumber);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
