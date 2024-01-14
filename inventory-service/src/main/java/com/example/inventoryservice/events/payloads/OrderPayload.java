package com.example.inventoryservice.events.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPayload {
  private Long orderNumber;
  private List<ProductPayload> products;
}
