package com.example.orderservice.service;

import com.example.orderservice.events.OrderPublisher;
import com.example.orderservice.events.payloads.ConfirmStockPayload;
import com.example.orderservice.exceptions.ExceptionType;
import com.example.orderservice.exceptions.OrderException;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.Product;
import com.example.orderservice.models.Status;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class OrderServiceTest {

  @Mock
  private OrderRepository repository;

  @Mock
  private OrderPublisher publisher;

  @InjectMocks
  private OrderServiceImpl service;
  private Order sampleOrder;
  private List<Product> sampleProduct;

  @BeforeEach
  void initObjectSamples() {
    sampleProduct = new ArrayList<>();
    sampleProduct.add(new Product("1A", 4L));

    sampleOrder = new Order();
    sampleOrder.setTitle("Title");
    sampleOrder.setDescription("Description");
    sampleOrder.setProducts(sampleProduct);
  }


  @Test
  void getSavedOrders() {
    List<Order> ordersData = new ArrayList<>();
    ordersData.add(sampleOrder);
    when(repository.findAll()).thenReturn(ordersData);

    List<Order> orders = service.getOrders();
    assertEquals(1, orders.size());
  }

  @Test
  void getSavedOrderById() {
    Long orderNumber = 500L;
    sampleOrder.setOrderNumber(orderNumber);
    when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.ofNullable(sampleOrder));
    Order order = assertDoesNotThrow(()->service.getOrderById(orderNumber));
    assertNotNull(order);
    assertEquals(orderNumber, order.getOrderNumber());
  }

  @Test
  void getExceptionWhenGettingOrderNotFound(){
    Long orderNumber = 500L;
    when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());
    OrderException exception = assertThrows( OrderException.class, ()->service.getOrderById(orderNumber));
    assertEquals(ExceptionType.NOT_FOUND_EXCEPTION.getName(), exception.getName() );
  }

  @Test
  void createOrder(){
    Long orderNumber = 500L;
    sampleOrder.setOrderNumber(orderNumber);
    when(repository.findFirstByOrderByIdDesc()).thenReturn(Optional.ofNullable(sampleOrder));

    Order newOrder = new Order();
    newOrder.setTitle("Title");
    newOrder.setDescription("Description");
    newOrder.setProducts(sampleProduct);

    when(repository.save(any(Order.class))).then(returnsFirstArg());
    Order order = service.createOrder(newOrder);

    verify(repository, times(1)).save(newOrder);
    assertNotNull(order);
    assertEquals(501L, order.getOrderNumber() );
    assertEquals(Status.PENDING, order.getStatus());
  }

  @Test
  void updateOrder() {
    Long orderNumber = 500L;
    sampleOrder.setOrderNumber(orderNumber);
    when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.ofNullable(sampleOrder));

    Order expectedOrder = new Order();
    expectedOrder.setOrderNumber(orderNumber);
    expectedOrder.setTitle("Title updated");
    expectedOrder.setDescription("Description updated");
    expectedOrder.setProducts(sampleProduct);

    assertDoesNotThrow(()->service.updateOrder(orderNumber, expectedOrder));
    verify(repository, times(1)).save(expectedOrder);

  }

  @Test
  void getExceptionWhenUpdatingOrderNotFound(){
    Long orderNumber = 500L;
    when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

    Order updOrder = new Order();
    updOrder.setTitle("Title updated");
    updOrder.setDescription("Description updated");
    updOrder.setProducts(sampleProduct);

    OrderException exception = assertThrows(OrderException.class, ()->service.updateOrder(orderNumber, updOrder));
    assertEquals(ExceptionType.NOT_FOUND_EXCEPTION.getName(), exception.getName());
  }

  @Test
  void deleteOrder() {
    Long orderNumber = 500L;
    sampleOrder.setOrderNumber(orderNumber);
    when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.ofNullable(sampleOrder));

    Order expectedOrder = new Order();
    expectedOrder.setTitle(sampleOrder.getTitle());
    expectedOrder.setOrderNumber(sampleOrder.getOrderNumber());
    expectedOrder.setDescription(sampleOrder.getDescription());
    expectedOrder.setProducts(sampleOrder.getProducts());
    expectedOrder.setStatus(Status.DELETED);

    assertDoesNotThrow(()->service.deleteOrder(orderNumber));
    verify(repository, times(1)).save(expectedOrder);

  }

  @Test
  void getExceptionWhenDeletingOrderNotFound() {
    Long orderNumber = 500L;
    when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

    OrderException exception = assertThrows(OrderException.class, ()->service.deleteOrder(orderNumber));
    assertEquals(ExceptionType.NOT_FOUND_EXCEPTION.getName(), exception.getName());
  }

  @Test
  void updateOrderStatus(){
    Long orderNumber = 500L;
    sampleOrder.setOrderNumber(orderNumber);
    when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.ofNullable(sampleOrder));

    String result = assertDoesNotThrow( () -> service.updateOrderStatus(new ConfirmStockPayload(orderNumber)));
    Order expectedOrder = new Order();
    expectedOrder.setTitle(sampleOrder.getTitle());
    expectedOrder.setOrderNumber(sampleOrder.getOrderNumber());
    expectedOrder.setDescription(sampleOrder.getDescription());
    expectedOrder.setProducts(sampleOrder.getProducts());
    expectedOrder.setStatus(Status.COMPLETED);

    verify(repository, times(1)).save(expectedOrder);
    assertEquals("OK", result);
  }

}
