package com.logistics.agent.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.logistics.agent")
public class AgentMcpApplication {

  public static void main(String[] args) {
    SpringApplication.run(AgentMcpApplication.class, args);
  }
}
