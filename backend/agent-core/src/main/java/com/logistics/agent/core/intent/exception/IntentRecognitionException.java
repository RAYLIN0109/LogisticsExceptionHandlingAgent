package com.logistics.agent.core.intent.exception;

public class IntentRecognitionException extends RuntimeException {

  public IntentRecognitionException(String message) {
    super(message);
  }

  public IntentRecognitionException(String message, Throwable cause) {
    super(message, cause);
  }
}
