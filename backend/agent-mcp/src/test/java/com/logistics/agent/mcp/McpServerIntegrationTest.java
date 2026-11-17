package com.logistics.agent.mcp;

import static org.assertj.core.api.Assertions.assertThat;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(
    classes = AgentMcpApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.lifecycle.timeout-per-shutdown-phase=1s"
)
class McpServerIntegrationTest {

  @LocalServerPort
  private int port;

  @Test
  void shouldDiscoverAndCallToolsOverMcpSse() {
    HttpClientSseClientTransport transport = HttpClientSseClientTransport
        .builder("http://127.0.0.1:" + port)
        .sseEndpoint("/mcp/sse")
        .build();
    McpSyncClient client = McpClient.sync(transport)
        .requestTimeout(Duration.ofSeconds(10))
        .build();

    try {
      client.initialize();

      assertThat(client.listTools().tools())
          .extracting(tool -> tool.name())
          .containsExactlyInAnyOrder(
              "get_waybill_info",
              "get_tracking_info",
              "get_customer_preference",
              "get_station_info",
              "recognize_call_intent"
          );

      CallToolResult waybillResult = client.callTool(new CallToolRequest(
          "get_waybill_info",
          Map.of("waybillNo", "YT123456789")
      ));
      assertThat(waybillResult.isError()).isFalse();
      assertThat(((TextContent) waybillResult.content().getFirst()).text())
          .contains("YT123456789")
          .contains("DELIVERING");

      CallToolResult intentResult = client.callTool(new CallToolRequest(
          "recognize_call_intent",
          Map.of(
              "waybillNo", "YT123456789",
              "callId", "CALL-MCP-001",
              "transcript", "我白天不在家，把快递放小区门卫"
          )
      ));
      assertThat(intentResult.isError()).isFalse();
      assertThat(((TextContent) intentResult.content().getFirst()).text())
          .contains("指定地点")
          .contains("小区门卫");
    } finally {
      client.close();
    }
  }
}
