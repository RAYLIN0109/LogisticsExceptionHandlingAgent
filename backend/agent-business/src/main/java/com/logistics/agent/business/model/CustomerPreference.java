package com.logistics.agent.business.model;

public record CustomerPreference(
    String waybillNo,
    String preferredLocationType,
    String preferredLocationDetail,
    String lastRackingValue,
    String lastCallId
) {
}
