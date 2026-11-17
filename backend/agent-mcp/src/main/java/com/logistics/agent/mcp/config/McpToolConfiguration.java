package com.logistics.agent.mcp.config;

import com.logistics.agent.mcp.tool.IntentRecognitionTools;
import com.logistics.agent.mcp.tool.LogisticsQueryTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class McpToolConfiguration {

  @Bean
  public ToolCallbackProvider logisticsMcpTools(
      LogisticsQueryTools logisticsQueryTools,
      IntentRecognitionTools intentRecognitionTools
  ) {
    return MethodToolCallbackProvider.builder()
        .toolObjects(logisticsQueryTools, intentRecognitionTools)
        .build();
  }
}
