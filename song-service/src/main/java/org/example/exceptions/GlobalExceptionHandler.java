package org.example.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String ERROR_CODE = "errorCode";
    public static final String ERROR_MESSAGE = "errorMessage";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        logger.error("Unhandled exception occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                ERROR_MESSAGE, e.getMessage(),
                ERROR_CODE, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleException(HttpClientErrorException e) {
        logger.info("handle HttpClientErrorException = {}", e.getMessage());
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
//for only one error
//                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Unknown error"),
//                        (existing, replacement) -> existing));
        logger.error("Validation error occurred", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                ERROR_MESSAGE, "Validation error",
                "details", errors,
                ERROR_CODE, Integer.toString(HttpStatus.BAD_REQUEST.value())
        ));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, String>> handleException(NumberFormatException e) {
        logger.info("handle NumberFormatException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_MESSAGE, e.getMessage(),
                ERROR_CODE, Integer.toString(HttpStatus.BAD_REQUEST.value())));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleException(NoSuchElementException e) {
        logger.info("handle NotFoundException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(GlobalExceptionHandler.ERROR_MESSAGE, e.getMessage(),
                GlobalExceptionHandler.ERROR_CODE, Integer.toString(HttpStatus.NOT_FOUND.value())));
    }

    @ExceptionHandler(KeyAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleException(KeyAlreadyExistsException e) {
        logger.info("handle KeyAlreadyExistsException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(GlobalExceptionHandler.ERROR_MESSAGE, e.getMessage(),
                GlobalExceptionHandler.ERROR_CODE, Integer.toString(HttpStatus.CONFLICT.value())));
    }
}
