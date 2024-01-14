package com.example.inventoryservice.services;

import com.example.inventoryservice.events.payloads.ProductPayload;
import com.example.inventoryservice.exceptions.ExceptionType;
import com.example.inventoryservice.exceptions.InventoryException;
import com.example.inventoryservice.models.Product;
import com.example.inventoryservice.models.Status;
import com.example.inventoryservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository repository;

  @Override
  public List<Product> getProducts() {
    log.info("Getting all products");
    return repository.findAll();
  }

  @Override
  public Product getProductByCode(String code) throws InventoryException {
    log.info("Getting product {}", code);
    return repository.findByCode(code).orElseThrow(()-> {
      log.error("Product {} was not found", code);
      return new InventoryException(ExceptionType.NOT_FOUND_EXCEPTION);
    });
  }

  @Override
  public Product addProduct(Product product) {
    log.info("Adding product {}", product.getCode());
    product.setStatus(evaluateStock(product.getStock()));
    Product newProduct = repository.save(product);
    log.info("Product {} has been created", newProduct.getCode());
    return newProduct;
  }

  @Override
  public void updateProduct(String code, Product product) throws InventoryException {
    log.info("Updating product {} ", code);
    Optional<Product> updProduct = repository.findByCode(code);
    if(updProduct.isPresent()){
      Product toUpdate = updProduct.get();
      toUpdate.setName(product.getName());
      toUpdate.setDescription(product.getDescription());
      toUpdate.setStock(product.getStock());
      toUpdate.setStatus(evaluateStock(product.getStock()));
      repository.save(toUpdate);
      log.info("Product {} has been updated", code);
    } else {
      log.error("Product with id {} not found", code);
      throw new InventoryException(ExceptionType.NOT_FOUND_EXCEPTION);
    }
  }

  @Override
  public void deleteProduct(String code) throws InventoryException{
    log.info("Deleting product {}", code);
    Optional<Product> delProduct = repository.findByCode(code);
    if (delProduct.isEmpty()) {
      throw new InventoryException(ExceptionType.NOT_FOUND_EXCEPTION);
    }
    Product product = delProduct.get();
    product.setStatus(Status.DELETED);
    repository.save(product);
    log.info("Product {} has been deleted", code);
  }

  @Override
  public void validateStock(ProductPayload productPayload) {
    log.info("Checking stock for product {}", productPayload.getCode());
    Optional<Product> productDb = repository.findByCode(productPayload.getCode());
    if(productDb.isEmpty()) return;
    Product productInventory = productDb.get();
    long stockLeft = productInventory.getStock() - productPayload.getAmount();
    if (stockLeft >= 0 ) {
      productInventory.setStock(stockLeft);
    } else {
      // Mocking request to an external provider for more products
      // when there's not enough by just setting twice what is missing.
      productInventory.setStock(Math.abs(stockLeft) * 2);
      log.info("Not enough stock, restocking with third party services");
    }
    productInventory.setStatus(evaluateStock(productInventory.getStock()));
    repository.save(productInventory);
    log.info("Stock updated, product {} now has {} items", productPayload.getCode(), productInventory.getStock());
  }


}
