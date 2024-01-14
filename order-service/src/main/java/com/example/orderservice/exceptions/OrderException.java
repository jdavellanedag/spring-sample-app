package com.example.orderservice.exceptions;

import org.springframework.http.HttpStatus;

public class OrderException extends RuntimeException{

  private ExceptionType exceptionType;
  private HttpStatus status;
  private String name;

  public OrderException() {
    super();
  }

  public OrderException(ExceptionType exceptionType){
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
