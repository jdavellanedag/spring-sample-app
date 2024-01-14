package com.example.orderservice.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
@Data
public class Order {

  @Id
  private String id;

  private String title;
  private Long orderNumber;
  private String description;
  private Status status;
  private List<Product> products;

}
