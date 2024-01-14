package com.example.inventoryservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InventoryExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = InventoryException.class)
  public ResponseEntity<ErrorResponse> handleException(InventoryException error) {
    return ResponseEntity.status(error.getStatus()).body(new ErrorResponse(error.getName(), error.getMessage()));
  }
}
