package com.logistics.agent.business.model;

public record WaybillInfo(
    String waybillNo,
    String productCode,
    boolean yzdFlag,
    String status,
    String receiverCommunity,
    String stationCode
) {
}
