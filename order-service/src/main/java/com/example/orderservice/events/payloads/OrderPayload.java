package com.example.orderservice.events.payloads;

import com.example.orderservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPayload {

  private Long orderNumber;
  private List<Product> products;

}
