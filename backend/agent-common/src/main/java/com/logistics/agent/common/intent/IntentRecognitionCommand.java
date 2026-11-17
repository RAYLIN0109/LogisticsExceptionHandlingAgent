package com.logistics.agent.common.intent;

public record IntentRecognitionCommand(
    String waybillNo,
    String callId,
    String transcript
) {
}
