package com.example.inventoryservice.events;

import com.example.inventoryservice.config.RabbitConfig;
import com.example.inventoryservice.events.payloads.ConfirmStockPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryPublisher {

  @Autowired
  private AmqpTemplate template;

  public void stockConfirmed(Long orderNumber) {
    String routingKey = "orders.confirm.stock";
    ConfirmStockPayload stockPayload = new ConfirmStockPayload(orderNumber);
    template.convertAndSend(RabbitConfig.EXCHANGE, routingKey, stockPayload);
    log.info("Message for order {} sent to rabbitMQ", orderNumber);
  }

}
