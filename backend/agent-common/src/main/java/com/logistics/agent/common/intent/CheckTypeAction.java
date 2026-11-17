package com.logistics.agent.common.intent;

public enum CheckTypeAction {
  IN_STATION("6"),
  RESET_CHECK("resetCheck");

  private final String checkType;

  CheckTypeAction(String checkType) {
    this.checkType = checkType;
  }

  public String checkType() {
    return checkType;
  }

  public static CheckTypeAction fromRackingValue(String value) {
    return RackingIntent.fromValue(value) == RackingIntent.IN_STATION ? IN_STATION : RESET_CHECK;
  }
}
