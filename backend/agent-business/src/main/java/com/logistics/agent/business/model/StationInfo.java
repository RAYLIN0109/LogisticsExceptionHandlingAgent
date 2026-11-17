package com.logistics.agent.business.model;

import java.util.List;

public record StationInfo(
    String stationCode,
    String stationName,
    boolean cabinetEnabled,
    List<String> excludedProducts,
    List<String> deliveryPolicies
) {
}
