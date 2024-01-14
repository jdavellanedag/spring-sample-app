package com.example.inventoryservice.events.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPayload {
  private String code;
  private Long amount;
}
