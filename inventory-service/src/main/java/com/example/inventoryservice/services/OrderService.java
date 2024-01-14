package com.example.inventoryservice.services;

import com.example.inventoryservice.events.payloads.OrderPayload;

public interface OrderService {
  Long processNewOrderInventory(OrderPayload payload);

  String confirmInventory(Long orderNumber);
}
