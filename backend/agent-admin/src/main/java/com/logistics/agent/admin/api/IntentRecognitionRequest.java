package com.logistics.agent.admin.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IntentRecognitionRequest(
    @NotBlank @Size(max = 64) String waybillNo,
    @NotBlank @Size(max = 128) String callId,
    @NotBlank @Size(max = 20000) String transcript
) {
}
