package com.example.inventoryservice.services;

import com.example.inventoryservice.events.InventoryPublisher;
import com.example.inventoryservice.events.payloads.OrderPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
  @Autowired
  private InventoryPublisher publisher;
  @Autowired
  private ProductService productService;

  @Override
  public Long processNewOrderInventory(OrderPayload payload) {
    log.info("Processing inventory for order {}", payload.getOrderNumber());
    payload.getProducts().forEach(productService::validateStock);
    return payload.getOrderNumber();
  }

  @Override
  public String confirmInventory(Long orderNumber) {
    publisher.stockConfirmed(orderNumber);
    return "OK";
  }

}
