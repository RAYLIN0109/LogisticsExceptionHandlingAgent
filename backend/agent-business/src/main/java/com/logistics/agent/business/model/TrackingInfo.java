package com.logistics.agent.business.model;

import java.util.List;

public record TrackingInfo(
    String waybillNo,
    List<TrackingEvent> events
) {

  public record TrackingEvent(
      String operationCode,
      String operationName,
      String operationTime
  ) {
  }
}
