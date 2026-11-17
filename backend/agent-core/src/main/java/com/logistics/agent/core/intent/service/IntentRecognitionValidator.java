package com.logistics.agent.core.intent.service;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;
import com.logistics.agent.common.intent.IntentRecognitionResult.ExtractedRequirement;
import com.logistics.agent.common.intent.RackingIntent;
import com.logistics.agent.core.intent.config.IntentRecognitionProperties;
import com.logistics.agent.core.intent.exception.IntentRecognitionException;
import org.springframework.stereotype.Component;

@Component
public class IntentRecognitionValidator {

  private final IntentRecognitionProperties properties;

  public IntentRecognitionValidator(IntentRecognitionProperties properties) {
    this.properties = properties;
  }

  public IntentRecognitionResult validate(
      IntentRecognitionCommand command,
      IntentRecognitionResult result
  ) {
    if (result == null) {
      throw new IntentRecognitionException("Model returned an empty recognition result");
    }
    if (!"Racking".equals(result.tagCode())) {
      throw new IntentRecognitionException("Model returned an invalid tagCode");
    }
    if (!Double.isFinite(result.confidence())
        || result.confidence() < 0
        || result.confidence() > 1) {
      throw new IntentRecognitionException("Recognition confidence must be between 0 and 1");
    }

    RackingIntent intent;
    try {
      intent = RackingIntent.fromValue(result.value());
    } catch (IllegalArgumentException exception) {
      throw new IntentRecognitionException("Model returned an unsupported Racking value", exception);
    }

    String evidence = result.contextSentence() == null ? "" : result.contextSentence().trim();
    if (evidence.isEmpty() || !command.transcript().contains(evidence)) {
      throw new IntentRecognitionException("Evidence sentence must be an exact excerpt of the transcript");
    }

    ExtractedRequirement extracted = result.extracted() == null
        ? new ExtractedRequirement(null, null, null)
        : result.extracted();
    boolean needHumanReview = result.needHumanReview()
        || result.confidence() < properties.confidenceThreshold();

    return new IntentRecognitionResult(
        "Racking",
        intent.value(),
        intent.intentName(),
        result.confidence(),
        evidence,
        extracted,
        needHumanReview,
        result.reason()
    );
  }
}
