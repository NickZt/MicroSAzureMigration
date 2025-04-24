package com.example.resource.exeptions;

import jakarta.validation.ConstraintViolationException;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.InvalidKeyException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String ERROR_UPLOADING_RESOURCE = "Error uploading resource";
    public static final String ERROR_CODE = "errorCode";
    public static final String ERROR_MESSAGE = "errorMessage";
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        logger.info("handle Exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(ERROR_MESSAGE,
                ERROR_UPLOADING_RESOURCE, ERROR_CODE, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleException(NoSuchElementException e) {
        logger.info("handle NotFoundException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(GlobalExceptionHandler.ERROR_MESSAGE, e.getMessage(),
                GlobalExceptionHandler.ERROR_CODE, Integer.toString(HttpStatus.NOT_FOUND.value())));
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<Map<String, String>> handleException(InvalidKeyException e) {
        logger.info("handle InvalidKeyException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(GlobalExceptionHandler.ERROR_MESSAGE, e.getMessage(),
                GlobalExceptionHandler.ERROR_CODE, Integer.toString(HttpStatus.BAD_REQUEST.value())));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleException(HttpClientErrorException e) {
        logger.info("handle HttpClientErrorException = {}", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<String> handleException(HttpServerErrorException e) {
        logger.info("handle HttpServerErrorException = {}", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        //most informative way is to use concatenation for field validation errors
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Unknown error"),
                        (existing, replacement) -> existing + "; " + replacement
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                ERROR_MESSAGE, "Validation error",
                "details", errors,
                ERROR_CODE, Integer.toString(HttpStatus.BAD_REQUEST.value())
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                ERROR_MESSAGE, "Invalid value '" + e.getValue() + "' for ID. Must be a positive integer",
                ERROR_CODE, Integer.toString(HttpStatus.BAD_REQUEST.value())
        ));
    }

    @ExceptionHandler(TikaException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(TikaException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                ERROR_MESSAGE, e.getMessage(),
                ERROR_CODE, Integer.toString(HttpStatus.BAD_REQUEST.value())
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                ERROR_MESSAGE,
                "Invalid value '" + e.getConstraintViolations().stream()
                        .map(v -> v.getInvalidValue().toString()).collect(Collectors.joining(
                                ",")) + "' for ID. Must be a positive integer",
                ERROR_CODE, Integer.toString(HttpStatus.BAD_REQUEST.value())
        ));
    }
}