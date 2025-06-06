package com.gevernova.addressbook.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Custom not found (e.g., address not found)
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleAddressNotFoundException(AddressNotFoundException ex, WebRequest request) {
        logger.warn("AddressNotFoundException: {}. URI: {}", ex.getMessage(), request.getDescription(false));
        return buildErrorResponse(ex.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    // 2. Validation error (for @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDetails> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation failed. URI: {}", request.getDescription(false));

        List<String> validationMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ValidationErrorDetails errorDetails = new ValidationErrorDetails(
                LocalDateTime.now(),
                "Validation failed",
                request.getDescription(false),
                validationMessages
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // 3. Entity not found by JPA
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        logger.warn("EntityNotFoundException: {}. URI: {}", ex.getMessage(), request.getDescription(false));
        return buildErrorResponse("Resource not found", request, HttpStatus.NOT_FOUND);
    }

    // 4. No such element (e.g., Optional.get() without check)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDetails> handleNoSuchElement(NoSuchElementException ex, WebRequest request) {
        logger.warn("NoSuchElementException: {}. URI: {}", ex.getMessage(), request.getDescription(false));
        return buildErrorResponse("Requested element not found", request, HttpStatus.NOT_FOUND);
    }

    // 5. Data constraint violations (e.g., duplicate key, null constraint)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("DataIntegrityViolationException: {}. URI: {}", ex.getMessage(), request.getDescription(false));
        return buildErrorResponse("Database constraint violation", request, HttpStatus.CONFLICT);
    }

    // 6. Catch-all for all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: {}. URI: {}", ex.getMessage(), request.getDescription(false), ex);
        return buildErrorResponse("Internal server error", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ===== Utility method to avoid duplication =====
    private ResponseEntity<ErrorDetails> buildErrorResponse(String message, WebRequest request, HttpStatus status) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, status);
    }

    // ===== Response DTOs =====
    @Getter
    @AllArgsConstructor
    public static class ErrorDetails {
        private final LocalDateTime timestamp;
        private final String message;
        private final String details;
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationErrorDetails {
        private final LocalDateTime timestamp;
        private final String message;
        private final String details;
        private final List<String> validationErrors;
    }
}
