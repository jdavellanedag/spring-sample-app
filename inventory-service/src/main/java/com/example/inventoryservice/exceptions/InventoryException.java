package com.example.inventoryservice.exceptions;

import org.springframework.http.HttpStatus;

public class InventoryException extends RuntimeException {
  private ExceptionType exceptionType;
  private HttpStatus status;
  private String name;

  public InventoryException() {
    super();
  }

  public InventoryException(ExceptionType exceptionType){
    super(exceptionType.getDescription());
    status = exceptionType.getStatus();
    name = exceptionType.getName();
  }

  public HttpStatus getStatus(){
    return status;
  }

  public String getName(){
    return name;
  }

}
