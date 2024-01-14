package com.example.inventoryservice.services;

import com.example.inventoryservice.events.payloads.ProductPayload;
import com.example.inventoryservice.exceptions.InventoryException;
import com.example.inventoryservice.models.Product;
import com.example.inventoryservice.models.Status;

import java.util.List;

public interface ProductService {
  List<Product> getProducts();

  Product getProductByCode(String code) throws InventoryException;

  Product addProduct(Product product);

  void updateProduct(String code, Product product) throws InventoryException;

  void deleteProduct(String code) throws InventoryException;

  void validateStock(ProductPayload productPayload);

  default Status evaluateStock(Long stock) {
    return stock > 0 ? Status.IN_STOCK : Status.OUT_OF_STOCK;
  }
}
