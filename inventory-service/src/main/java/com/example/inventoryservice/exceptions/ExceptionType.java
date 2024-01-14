package com.example.inventoryservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {
  NOT_FOUND_EXCEPTION("NOT_FOUND_EXCEPTION", "The requested resource was not found", HttpStatus.NOT_FOUND),
  INTERNAL_SERVICE_EXCEPTION("INTERNAL_SERVICE_EXCEPTION", "Oops something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String name;
  private final String description;
  private final HttpStatus status;

}
