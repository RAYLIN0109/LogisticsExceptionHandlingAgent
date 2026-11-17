package com.logistics.agent.core.intent.service;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;
import com.logistics.agent.core.intent.config.IntentRecognitionProperties;
import com.logistics.agent.core.intent.exception.IntentRecognitionException;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "agent.intent", name = "provider", havingValue = "openai")
public class OpenAiIntentRecognitionService implements IntentRecognitionService {

  private final ChatClient chatClient;
  private final IntentRecognitionProperties properties;
  private final IntentRecognitionValidator validator;
  private final PromptTemplate promptTemplate;

  public OpenAiIntentRecognitionService(
      ChatClient.Builder chatClientBuilder,
      IntentRecognitionProperties properties,
      IntentRecognitionValidator validator,
      @Value("classpath:/prompts/intent-recognition.txt") Resource promptResource
  ) {
    this.chatClient = chatClientBuilder.build();
    this.properties = properties;
    this.validator = validator;
    this.promptTemplate = new PromptTemplate(promptResource);
  }

  @Override
  public IntentRecognitionResult recognize(IntentRecognitionCommand command) {
    String prompt = promptTemplate.render(Map.of(
        "waybillNo", command.waybillNo(),
        "callId", command.callId(),
        "transcript", command.transcript()
    ));

    try {
      IntentRecognitionResult result = chatClient.prompt()
          .options(OpenAiChatOptions.builder()
              .model(properties.model())
              .temperature(properties.temperature())
              .build())
          .user(prompt)
          .call()
          .entity(IntentRecognitionResult.class);
      return validator.validate(command, result);
    } catch (IntentRecognitionException exception) {
      throw exception;
    } catch (Exception exception) {
      throw new IntentRecognitionException("LLM intent recognition failed", exception);
    }
  }
}
