package com.logistics.agent.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.logistics.agent")
public class AgentAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(AgentAdminApplication.class, args);
  }
}
