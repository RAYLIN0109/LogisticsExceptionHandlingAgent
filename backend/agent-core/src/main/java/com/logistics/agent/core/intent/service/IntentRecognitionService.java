package com.logistics.agent.core.intent.service;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;

public interface IntentRecognitionService {

  IntentRecognitionResult recognize(IntentRecognitionCommand command);
}
