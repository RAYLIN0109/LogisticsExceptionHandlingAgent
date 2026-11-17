package com.logistics.agent.common.intent;

import java.util.Arrays;

public enum RackingIntent {
  ON_DOOR("1", "上门"),
  IN_STATION("2", "入库入柜"),
  DESIGNATED_LOCATION("3", "指定地点"),
  GUIDE_EVASION("4", "引导推诿"),
  SYSTEM_AUDIO("5", "系统音"),
  ONE_SIDE_ONLY("6", "仅一方发言"),
  SEMANTIC_CONFUSION("7", "语义混乱"),
  UNRELATED("8", "与快递无关");

  private final String value;
  private final String intentName;

  RackingIntent(String value, String intentName) {
    this.value = value;
    this.intentName = intentName;
  }

  public String value() {
    return value;
  }

  public String intentName() {
    return intentName;
  }

  public static RackingIntent fromValue(String value) {
    return Arrays.stream(values())
        .filter(intent -> intent.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unsupported Racking value: " + value));
  }
}
