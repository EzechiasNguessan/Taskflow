package com.taskflow.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(TaskNotFoundException.class)
   public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now());
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
   }

   @ExceptionHandler(ProjectNotFoundException.class)
   public ResponseEntity<ErrorResponse> handleProjectNotFound(ProjectNotFoundException ex) {
      ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now());
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
   }
}
