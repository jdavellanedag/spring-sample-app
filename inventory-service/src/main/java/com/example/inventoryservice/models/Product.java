package com.example.inventoryservice.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
public class Product {

  @Id
  private String id;

  private String code;
  private String name;
  private String description;
  private Long stock;
  private Status status;
}
