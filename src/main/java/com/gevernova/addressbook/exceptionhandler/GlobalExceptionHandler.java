package com.gevernova.addressbook.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice // Enables this class to handle exceptions across the entire application
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handles AddressNotFoundException and returns a 404 Not Found
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleAddressNotFoundException(AddressNotFoundException addressNotFoundException, WebRequest request) {
        logger.warn("AddressNotFoundException: {}. Request URI: {}", addressNotFoundException.getMessage(), request.getDescription(false));
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                addressNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Handles UserNotFoundException and returns a 404 Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException userNotFoundException, WebRequest request) {
        logger.warn("UserNotFoundException: {}. Request URI: {}", userNotFoundException.getMessage(), request.getDescription(false));
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                userNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Handles validation errors (e.g., @NotBlank, @Email) and returns a 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            logger.warn("Validation error in field '{}': {}", error.getField(), error.getDefaultMessage());
        });

        // For validation errors, it's often more useful to return the map of field errors directly
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handles all other unhandled exceptions and returns a 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: {}. Request URI: {}", ex.getMessage(), request.getDescription(false), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    public static class ErrorDetails {
        // Getters
        private LocalDateTime timestamp;
        private String message;
        private String details;

        public ErrorDetails(LocalDateTime timestamp, String message, String details) {
            this.timestamp = timestamp;
            this.message = message;
            this.details = details;
        }

    }
}