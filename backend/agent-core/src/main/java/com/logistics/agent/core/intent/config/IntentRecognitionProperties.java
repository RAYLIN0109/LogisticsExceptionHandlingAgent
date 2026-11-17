package com.logistics.agent.core.intent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agent.intent")
public record IntentRecognitionProperties(
    String provider,
    double confidenceThreshold,
    String model,
    double temperature
) {

  public IntentRecognitionProperties {
    if (confidenceThreshold < 0 || confidenceThreshold > 1) {
      throw new IllegalArgumentException("agent.intent.confidence-threshold must be between 0 and 1");
    }
    if (temperature < 0 || temperature > 2) {
      throw new IllegalArgumentException("agent.intent.temperature must be between 0 and 2");
    }
  }
}
