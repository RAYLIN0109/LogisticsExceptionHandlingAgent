package com.logistics.agent.core.intent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;
import com.logistics.agent.common.intent.IntentRecognitionResult.ExtractedRequirement;
import com.logistics.agent.core.intent.config.IntentRecognitionProperties;
import com.logistics.agent.core.intent.exception.IntentRecognitionException;
import org.junit.jupiter.api.Test;

class IntentRecognitionValidatorTest {

  private final IntentRecognitionValidator validator = new IntentRecognitionValidator(
      new IntentRecognitionProperties("openai", 0.75, "test-model", 0.0)
  );
  private final IntentRecognitionCommand command =
      new IntentRecognitionCommand("YT123456789", "CALL-001", "请送货上门");

  @Test
  void shouldForceHumanReviewBelowThreshold() {
    IntentRecognitionResult result = result("1", 0.60, "请送货上门", false);

    IntentRecognitionResult validated = validator.validate(command, result);

    assertThat(validated.needHumanReview()).isTrue();
    assertThat(validated.intentName()).isEqualTo("上门");
  }

  @Test
  void shouldRejectEvidenceOutsideTranscript() {
    IntentRecognitionResult result = result("1", 0.95, "模型编造的证据", false);

    assertThatThrownBy(() -> validator.validate(command, result))
        .isInstanceOf(IntentRecognitionException.class)
        .hasMessageContaining("exact excerpt");
  }

  @Test
  void shouldRejectUnsupportedLabel() {
    IntentRecognitionResult result = result("9", 0.95, "请送货上门", false);

    assertThatThrownBy(() -> validator.validate(command, result))
        .isInstanceOf(IntentRecognitionException.class)
        .hasMessageContaining("unsupported Racking value");
  }

  private IntentRecognitionResult result(
      String value,
      double confidence,
      String evidence,
      boolean needHumanReview
  ) {
    return new IntentRecognitionResult(
        "Racking",
        value,
        "任意名称",
        confidence,
        evidence,
        new ExtractedRequirement(null, null, null),
        needHumanReview,
        "测试"
    );
  }
}
