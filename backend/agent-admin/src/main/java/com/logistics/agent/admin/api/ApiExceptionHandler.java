package com.logistics.agent.admin.api;

import com.logistics.agent.core.intent.exception.IntentRecognitionException;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
    String message = exception.getBindingResult().getFieldErrors().stream()
        .map(this::formatFieldError)
        .collect(Collectors.joining("; "));
    return buildError(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", message);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleUnreadableMessage() {
    return buildError(HttpStatus.BAD_REQUEST, "INVALID_JSON", "Request body is not valid JSON");
  }

  @ExceptionHandler(IntentRecognitionException.class)
  public ResponseEntity<ApiError> handleRecognition(IntentRecognitionException exception) {
    return buildError(HttpStatus.BAD_GATEWAY, "INTENT_RECOGNITION_FAILED", exception.getMessage());
  }

  private String formatFieldError(FieldError error) {
    return error.getField() + ": " + error.getDefaultMessage();
  }

  private ResponseEntity<ApiError> buildError(HttpStatus status, String code, String message) {
    return ResponseEntity.status(status)
        .body(new ApiError(Instant.now(), status.value(), code, message));
  }
}
