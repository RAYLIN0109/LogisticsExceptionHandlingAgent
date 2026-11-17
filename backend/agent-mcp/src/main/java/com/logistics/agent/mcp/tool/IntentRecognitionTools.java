package com.logistics.agent.mcp.tool;

import com.logistics.agent.common.intent.IntentRecognitionCommand;
import com.logistics.agent.common.intent.IntentRecognitionResult;
import com.logistics.agent.core.intent.service.IntentRecognitionService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class IntentRecognitionTools {

  private final IntentRecognitionService intentRecognitionService;

  public IntentRecognitionTools(IntentRecognitionService intentRecognitionService) {
    this.intentRecognitionService = intentRecognitionService;
  }

  @Tool(name = "recognize_call_intent", description = "识别派件电联转写文本的 Racking 八类意图，返回证据、置信度和审核标记")
  public IntentRecognitionResult recognizeCallIntent(
      @ToolParam(description = "运单号") String waybillNo,
      @ToolParam(description = "电联编号") String callId,
      @ToolParam(description = "完整通话转写文本") String transcript
  ) {
    return intentRecognitionService.recognize(
        new IntentRecognitionCommand(waybillNo, callId, transcript)
    );
  }
}
