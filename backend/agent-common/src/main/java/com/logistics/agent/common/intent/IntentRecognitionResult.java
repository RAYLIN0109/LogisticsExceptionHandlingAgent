package com.logistics.agent.common.intent;

public record IntentRecognitionResult(
    String tagCode,
    String value,
    String intentName,
    double confidence,
    String contextSentence,
    ExtractedRequirement extracted,
    boolean needHumanReview,
    String reason
) {

  public record ExtractedRequirement(
      String locationType,
      String locationDetail,
      String timePreference
  ) {
  }
}
