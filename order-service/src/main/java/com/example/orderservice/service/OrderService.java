package com.example.orderservice.service;

import com.example.orderservice.events.payloads.ConfirmStockPayload;
import com.example.orderservice.exceptions.OrderException;
import com.example.orderservice.models.Order;

import java.util.List;

public interface OrderService {
  List<Order> getOrders();

  Order getOrderById(Long orderNumber) throws OrderException;

  Order createOrder(Order order);

  void updateOrder(Long orderNumber, Order order) throws OrderException;

  void deleteOrder(Long orderNumber) throws OrderException;

  String updateOrderStatus(ConfirmStockPayload payload);
}
