package com.logistics.agent.business.service;

import com.logistics.agent.business.model.CustomerPreference;
import com.logistics.agent.business.model.StationInfo;
import com.logistics.agent.business.model.TrackingInfo;
import com.logistics.agent.business.model.TrackingInfo.TrackingEvent;
import com.logistics.agent.business.model.WaybillInfo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MockLogisticsQueryService implements LogisticsQueryService {

  public static final String DEMO_WAYBILL_NO = "YT123456789";

  private static final WaybillInfo DEMO_WAYBILL = new WaybillInfo(
      DEMO_WAYBILL_NO,
      "YZD",
      true,
      "DELIVERING",
      "山水小区",
      "STATION-SH-001"
  );

  @Override
  public WaybillInfo getWaybillInfo(String waybillNo) {
    requireDemoWaybill(waybillNo);
    return DEMO_WAYBILL;
  }

  @Override
  public TrackingInfo getTrackingInfo(String waybillNo) {
    requireDemoWaybill(waybillNo);
    return new TrackingInfo(waybillNo, List.of(
        new TrackingEvent("710", "派件上车", "2026-11-17 08:30:00"),
        new TrackingEvent("CALL_ALL_MSG", "95号电联完成", "2026-11-17 09:05:00")
    ));
  }

  @Override
  public CustomerPreference getCustomerPreference(String waybillNo) {
    requireDemoWaybill(waybillNo);
    return new CustomerPreference(
        waybillNo,
        "门卫",
        "小区门卫",
        "3",
        "CALL-HISTORY-001"
    );
  }

  @Override
  public StationInfo getStationInfo(String waybillNo) {
    requireDemoWaybill(waybillNo);
    return new StationInfo(
        DEMO_WAYBILL.stationCode(),
        "上海演示网点",
        true,
        List.of("FRESH"),
        List.of(
            "山水小区包裹应优先放物业快递柜，放门卫前需人工确认",
            "圆准达产品入柜前必须完成有效电联"
        )
    );
  }

  private void requireDemoWaybill(String waybillNo) {
    if (waybillNo == null || waybillNo.isBlank()) {
      throw new IllegalArgumentException("waybillNo must not be blank");
    }
    if (!DEMO_WAYBILL_NO.equals(waybillNo)) {
      throw new IllegalArgumentException("Mock waybill not found: " + waybillNo);
    }
  }
}
