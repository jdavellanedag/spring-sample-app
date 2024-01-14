package com.example.orderservice.service;

import com.example.orderservice.events.payloads.ConfirmStockPayload;
import com.example.orderservice.events.OrderPublisher;
import com.example.orderservice.exceptions.ExceptionType;
import com.example.orderservice.exceptions.OrderException;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.Status;
import com.example.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository repository;

  @Autowired
  private OrderPublisher publisher;

  @Override
  public List<Order> getOrders(){
    log.info("Getting all orders");
    return repository.findAll();
  }

  @Override
  public Order getOrderById(Long orderNumber) throws OrderException {
    log.info("Getting order {}", orderNumber);
    Optional<Order> order = repository.findByOrderNumber(orderNumber);
    return order.orElseThrow( () -> {
      log.error("Order with id {} not found", orderNumber);
      return new OrderException(ExceptionType.NOT_FOUND_EXCEPTION);
    });
  }

  @Override
  public Order createOrder(Order order) {
    log.info("Creating a new order");
    Optional<Order> lastOrderCreated = repository.findFirstByOrderByIdDesc();
    if (lastOrderCreated.isEmpty()) {
      order.setOrderNumber(100L);
    } else {
      order.setOrderNumber(lastOrderCreated.get().getOrderNumber() + 1);
    }
    order.setStatus(Status.PENDING);
    Order newOrder = repository.save(order);
    publisher.newOrderCreated(newOrder);
    log.info("Order {} has been created", newOrder.getOrderNumber());
    return newOrder;
  }

  @Override
  public void updateOrder(Long orderNumber, Order order) throws OrderException  {
    log.info("Updating order {}", orderNumber);
    Optional<Order> updOrder = repository.findByOrderNumber(orderNumber);
    if (updOrder.isPresent()){
      Order toUpdate = updOrder.get();
      toUpdate.setTitle(order.getTitle());
      toUpdate.setDescription(order.getDescription());
      toUpdate.setProducts(order.getProducts());
      repository.save(toUpdate);
      log.info("Order {} has been updated", orderNumber);
    } else {
      log.error("Order with id {} not found", orderNumber);
      throw new OrderException(ExceptionType.NOT_FOUND_EXCEPTION);
    }
  }

  @Override
  public void deleteOrder(Long orderNumber) throws OrderException{
    log.info("Deleting order {}", orderNumber);
    Optional<Order> delOrder = repository.findByOrderNumber(orderNumber);
    if (delOrder.isEmpty()) {
      log.error("Order with id {} not found", orderNumber);
      throw new OrderException(ExceptionType.NOT_FOUND_EXCEPTION);
    }
    Order order = delOrder.get();
    order.setStatus(Status.DELETED);
    repository.save(order);
    log.error("Order with id {} has been deleted", orderNumber);
  }

  @Override
  public String updateOrderStatus(ConfirmStockPayload payload) {
    Optional<Order> orderDb = repository.findByOrderNumber(payload.getOrderNumber());
    if (orderDb.isEmpty()) {
      return "SKIPPED";
    }
    Order order = orderDb.get();
    order.setStatus(Status.COMPLETED);
    repository.save(order);
    log.info("Inventory for order {} has been confirmed", order.getOrderNumber());
    return "OK";
  }

}
