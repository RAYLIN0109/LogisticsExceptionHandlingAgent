package com.logistics.agent.core.intent.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;
import com.logistics.agent.core.intent.config.IntentRecognitionProperties;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockIntentRecognitionServiceTest {

  private MockIntentRecognitionService service;

  @BeforeEach
  void setUp() {
    IntentRecognitionProperties properties =
        new IntentRecognitionProperties("mock", 0.75, "test-model", 0.0);
    service = new MockIntentRecognitionService(new IntentRecognitionValidator(properties));
  }

  @Test
  void shouldRecognizeEightIntentTypes() {
    Map<String, String> samples = new LinkedHashMap<>();
    samples.put("家里有人，请送货上门并本人签收", "1");
    samples.put("放在驿站快递柜就可以", "2");
    samples.put("我白天不在家，把快递放小区门卫", "3");
    samples.put("不要问我，请联系商家处理", "4");
    samples.put("您拨打的电话正在通话中", "5");
    samples.put("喂？喂？喂？无人回应", "6");
    samples.put("语音断句严重，完全听不清", "7");
    samples.put("您好，需要办理贷款吗", "8");

    samples.forEach((transcript, expectedValue) -> {
      IntentRecognitionResult result = service.recognize(command(transcript));
      assertThat(result.value()).as(transcript).isEqualTo(expectedValue);
      assertThat(result.contextSentence()).isEqualTo(transcript);
    });
  }

  @Test
  void shouldExtractDesignatedLocationAndTime() {
    IntentRecognitionResult result = service.recognize(command("我白天不在家，把快递放小区门卫"));

    assertThat(result.value()).isEqualTo("3");
    assertThat(result.extracted().locationType()).isEqualTo("门卫");
    assertThat(result.extracted().locationDetail()).isEqualTo("小区门卫");
    assertThat(result.extracted().timePreference()).isEqualTo("白天不在家");
    assertThat(result.needHumanReview()).isFalse();
  }

  @Test
  void shouldSendUncertainTextToHumanReview() {
    IntentRecognitionResult result = service.recognize(command("没有可理解内容的普通一句话"));

    assertThat(result.value()).isEqualTo("7");
    assertThat(result.confidence()).isEqualTo(0.35);
    assertThat(result.needHumanReview()).isTrue();
  }

  private IntentRecognitionCommand command(String transcript) {
    return new IntentRecognitionCommand("YT123456789", "CALL-001", transcript);
  }
}
