package com.example.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

  private String code;
  private Long amount;

}
