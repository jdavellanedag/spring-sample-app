package com.example.orderservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = OrderException.class)
  public ResponseEntity<ErrorResponse> handleException(OrderException error) {
    return ResponseEntity.status(error.getStatus()).body(new ErrorResponse(error.getName(), error.getMessage()));
  }

}
