package com.example.orderservice.events;

import com.example.orderservice.config.RabbitConfig;
import com.example.orderservice.events.payloads.OrderPayload;
import com.example.orderservice.models.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderPublisher {

  @Autowired
  private AmqpTemplate template;

  public void newOrderCreated(Order order) {
    String routingKey = "orders.check.stock";
    OrderPayload newOrderCreated = new OrderPayload(order.getOrderNumber(), order.getProducts());
    template.convertAndSend(RabbitConfig.EXCHANGE, routingKey, newOrderCreated);
    log.info("Message for order {} sent to rabbitMQ", order.getOrderNumber());
  }

}
