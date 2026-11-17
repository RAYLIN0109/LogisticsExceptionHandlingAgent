package com.logistics.agent.admin.api;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;
import com.logistics.agent.core.intent.service.IntentRecognitionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/intent")
public class IntentRecognitionController {

  private final IntentRecognitionService intentRecognitionService;

  public IntentRecognitionController(IntentRecognitionService intentRecognitionService) {
    this.intentRecognitionService = intentRecognitionService;
  }

  @PostMapping("/recognize")
  public IntentRecognitionResult recognize(@Valid @RequestBody IntentRecognitionRequest request) {
    return intentRecognitionService.recognize(new IntentRecognitionCommand(
        request.waybillNo(),
        request.callId(),
        request.transcript()
    ));
  }
}
