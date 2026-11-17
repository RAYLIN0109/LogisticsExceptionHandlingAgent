package com.logistics.agent.core.intent.service;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;
import com.logistics.agent.common.intent.IntentRecognitionResult.ExtractedRequirement;
import com.logistics.agent.common.intent.RackingIntent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
    prefix = "agent.intent",
    name = "provider",
    havingValue = "mock",
    matchIfMissing = true
)
public class MockIntentRecognitionService implements IntentRecognitionService {

  private final IntentRecognitionValidator validator;

  public MockIntentRecognitionService(IntentRecognitionValidator validator) {
    this.validator = validator;
  }

  @Override
  public IntentRecognitionResult recognize(IntentRecognitionCommand command) {
    String transcript = command.transcript();
    RackingIntent intent = classify(transcript);
    double confidence = intent == RackingIntent.SEMANTIC_CONFUSION ? 0.35 : 0.93;

    IntentRecognitionResult result = new IntentRecognitionResult(
        "Racking",
        intent.value(),
        intent.intentName(),
        confidence,
        transcript,
        extract(transcript, intent),
        confidence < 0.75,
        "本地 Mock 根据演示语料关键词分类"
    );
    return validator.validate(command, result);
  }

  private RackingIntent classify(String text) {
    if (containsAny(text, "您拨打的电话", "已停机", "是空号", "正在通话中", "系统提示音")) {
      return RackingIntent.SYSTEM_AUDIO;
    }
    if (containsAny(text, "外卖订单", "保险", "贷款", "装修", "与快递无关")) {
      return RackingIntent.UNRELATED;
    }
    if (containsAny(text, "仅一方发言", "无人回应", "喂？喂？喂？")) {
      return RackingIntent.ONE_SIDE_ONLY;
    }
    if (containsAny(text, "语义混乱", "听不清", "乱码")) {
      return RackingIntent.SEMANTIC_CONFUSION;
    }
    if (containsAny(text, "引导推诿", "联系商家", "找平台客服", "不要问我")) {
      return RackingIntent.GUIDE_EVASION;
    }
    if (containsAny(text, "门卫", "前台", "邻居", "门口", "指定位置")) {
      return RackingIntent.DESIGNATED_LOCATION;
    }
    if (containsAny(text, "快递柜", "驿站", "入库", "入柜")) {
      return RackingIntent.IN_STATION;
    }
    if (containsAny(text, "上门", "送到家", "本人签收")) {
      return RackingIntent.ON_DOOR;
    }
    return RackingIntent.SEMANTIC_CONFUSION;
  }

  private ExtractedRequirement extract(String text, RackingIntent intent) {
    if (intent != RackingIntent.DESIGNATED_LOCATION && intent != RackingIntent.IN_STATION) {
      return new ExtractedRequirement(null, null, null);
    }

    String locationType = null;
    String locationDetail = null;
    if (text.contains("门卫")) {
      locationType = "门卫";
      locationDetail = text.contains("小区门卫") ? "小区门卫" : "门卫";
    } else if (text.contains("前台")) {
      locationType = "前台";
      locationDetail = "前台";
    } else if (text.contains("邻居")) {
      locationType = "邻居家";
      locationDetail = "邻居家";
    } else if (text.contains("门口")) {
      locationType = "门口";
      locationDetail = "门口";
    } else if (text.contains("快递柜")) {
      locationType = "快递柜";
      locationDetail = "快递柜";
    } else if (text.contains("驿站")) {
      locationType = "驿站";
      locationDetail = "驿站";
    }

    String timePreference = text.contains("白天不在家") ? "白天不在家" : null;
    return new ExtractedRequirement(locationType, locationDetail, timePreference);
  }

  private boolean containsAny(String text, String... keywords) {
    for (String keyword : keywords) {
      if (text.contains(keyword)) {
        return true;
      }
    }
    return false;
  }
}
