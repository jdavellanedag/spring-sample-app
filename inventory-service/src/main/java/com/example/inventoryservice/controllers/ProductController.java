package com.example.inventoryservice.controllers;

import com.example.inventoryservice.models.Product;
import com.example.inventoryservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService service;

  @GetMapping
  public ResponseEntity<List<Product>> getAll() {
    return ResponseEntity.ok(service.getProducts());
  }

  @GetMapping("/{code}")
  public ResponseEntity<Product> getProduct(@PathVariable String code) {
    return ResponseEntity.ok(service.getProductByCode(code));
  }

  @PostMapping
  public ResponseEntity<Product> addProduct(@RequestBody Product product) {
    Product newProduct = service.addProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
  }

  @PutMapping("/{code}")
  public ResponseEntity<Void> updateOrder(@PathVariable String code, @RequestBody Product product) {
    service.updateProduct(code, product);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{code}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String code){
    service.deleteProduct(code);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
