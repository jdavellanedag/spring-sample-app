package com.example.inventoryservice.services;

import com.example.inventoryservice.events.payloads.ProductPayload;
import com.example.inventoryservice.exceptions.ExceptionType;
import com.example.inventoryservice.exceptions.InventoryException;
import com.example.inventoryservice.models.Product;
import com.example.inventoryservice.models.Status;
import com.example.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository repository;

  @InjectMocks
  private ProductServiceImpl service;

  private Product productSample;

  @BeforeEach

  void initObjectSamples() {
    productSample = new Product();
    productSample.setName("Sample 1");
    productSample.setDescription("Description Sample 1");
    productSample.setCode("1A");
  }

  @Test
  void getSavedProducts() {
    List<Product> productsData = new ArrayList<>();
    productsData.add(productSample);
    when(repository.findAll()).thenReturn(productsData);

    List<Product> products = service.getProducts();
    assertEquals(1, products.size());
  }

  @Test
  void getSavedProductById() {
    String code = "1A";
    when(repository.findByCode(code)).thenReturn(Optional.ofNullable(productSample));

    Product product =  assertDoesNotThrow(() -> service.getProductByCode(code));
    assertNotNull(product);
    assertEquals(code, product.getCode());
  }

  @Test
  void getExceptionWhenProductNotFound() {
    String code = "1A";
    when(repository.findByCode(code)).thenReturn(Optional.empty());

    InventoryException exception =  assertThrows( InventoryException.class, () -> service.getProductByCode(code));
    assertEquals(ExceptionType.NOT_FOUND_EXCEPTION.getName(), exception.getName());
  }

  @ParameterizedTest
  @ValueSource(longs = {0, 1})
  void addProduct(Long stock) {
    productSample.setStock(stock);
    when(repository.save(any(Product.class))).then(returnsFirstArg());

    Product product = service.addProduct(productSample);
    verify(repository, times(1)).save(productSample);
    assertNotNull(product);
    if (stock > 0 ){
      assertEquals(Status.IN_STOCK, product.getStatus());
    } else {
      assertEquals(Status.OUT_OF_STOCK, product.getStatus());
    }
  }

  @ParameterizedTest
  @ValueSource(longs = {0, 1})
  void updateProduct(Long stock) {
    String code = "1A";
    productSample.setStock(3L);
    when(repository.findByCode(code)).thenReturn(Optional.ofNullable(productSample));

    Product expectedProduct = new Product();
    expectedProduct.setCode(code);
    expectedProduct.setName("Sample 1 updated");
    expectedProduct.setDescription("Description Sample 1 updated");
    expectedProduct.setStock(stock);
    if (stock > 0 ){
      expectedProduct.setStatus(Status.IN_STOCK);
    } else {
      expectedProduct.setStatus(Status.OUT_OF_STOCK);
    }

    assertDoesNotThrow(()->service.updateProduct(code, expectedProduct));
    verify(repository, times(1)).save(expectedProduct);
  }

  @Test
  void getExceptionWhenUpdatingProductNotFound() {
    String code = "1A";
    when(repository.findByCode(code)).thenReturn(Optional.empty());

    Product updProduct = new Product();
    updProduct.setName("Sample 1 updated");
    updProduct.setDescription("Description Sample 1 updated");

    InventoryException exception =  assertThrows( InventoryException.class, () -> service.updateProduct(code, updProduct));
    assertEquals(ExceptionType.NOT_FOUND_EXCEPTION.getName(), exception.getName());

  }

  @Test
  void deleteProduct() {
    String code = "1A";
    productSample.setStatus(Status.IN_STOCK);
    when(repository.findByCode(code)).thenReturn(Optional.ofNullable(productSample));

    Product expectedProduct = new Product();
    expectedProduct.setCode(code);
    expectedProduct.setName("Sample 1");
    expectedProduct.setDescription("Description Sample 1");
    expectedProduct.setStatus(Status.DELETED);

    assertDoesNotThrow(()-> service.deleteProduct(code));
    verify(repository, times(1)).save(expectedProduct);

  }

  @Test
  void getExceptionWhenDeletingProductNotFound() {
    String code = "1A";
    when(repository.findByCode(code)).thenReturn(Optional.empty());

    InventoryException exception =  assertThrows( InventoryException.class, () -> service.deleteProduct(code));
    assertEquals(ExceptionType.NOT_FOUND_EXCEPTION.getName(), exception.getName());
  }

  @ParameterizedTest
  @CsvSource({"1,1", "3,1", "3,3", "3,4", "0,1", "2,5"})
  void validateAndUpdateStock(Long stock, Long amount) {
    String code = "1A";
    productSample.setStock(stock);
    if (stock > 0){
      productSample.setStatus(Status.IN_STOCK);
    } else {
      productSample.setStatus(Status.OUT_OF_STOCK);
    }
    when(repository.findByCode(code)).thenReturn(Optional.ofNullable(productSample));

    ProductPayload payload = new ProductPayload();
    payload.setCode(code);
    payload.setAmount(amount);

    service.validateStock(payload);

    Product expectedProduct = new Product();
    expectedProduct.setCode(productSample.getCode());
    expectedProduct.setName(productSample.getName());
    expectedProduct.setDescription(productSample.getDescription());
    expectedProduct.setStock(productSample.getStock());
    if ( productSample.getStock() > 0) {
      expectedProduct.setStatus(Status.IN_STOCK);
    } else {
      expectedProduct.setStatus(Status.OUT_OF_STOCK);
    }

    verify(repository, times(1)).save(expectedProduct);

  }

}
