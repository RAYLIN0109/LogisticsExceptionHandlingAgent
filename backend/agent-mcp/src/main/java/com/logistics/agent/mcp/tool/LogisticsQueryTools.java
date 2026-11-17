package com.logistics.agent.mcp.tool;

import com.logistics.agent.business.model.CustomerPreference;
import com.logistics.agent.business.model.StationInfo;
import com.logistics.agent.business.model.TrackingInfo;
import com.logistics.agent.business.model.WaybillInfo;
import com.logistics.agent.business.service.LogisticsQueryService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class LogisticsQueryTools {

  private final LogisticsQueryService logisticsQueryService;

  public LogisticsQueryTools(LogisticsQueryService logisticsQueryService) {
    this.logisticsQueryService = logisticsQueryService;
  }

  @Tool(name = "get_waybill_info", description = "根据运单号查询产品标识、运单状态、收件小区和配送网点")
  public WaybillInfo getWaybillInfo(
      @ToolParam(description = "运单号，例如 YT123456789") String waybillNo
  ) {
    return logisticsQueryService.getWaybillInfo(waybillNo);
  }

  @Tool(name = "get_tracking_info", description = "根据运单号查询物流操作轨迹，包括派件上车和电联记录")
  public TrackingInfo getTrackingInfo(
      @ToolParam(description = "运单号，例如 YT123456789") String waybillNo
  ) {
    return logisticsQueryService.getTrackingInfo(waybillNo);
  }

  @Tool(name = "get_customer_preference", description = "查询客户配送偏好、最近电联意图和偏好投递地点")
  public CustomerPreference getCustomerPreference(
      @ToolParam(description = "运单号，例如 YT123456789") String waybillNo
  ) {
    return logisticsQueryService.getCustomerPreference(waybillNo);
  }

  @Tool(name = "get_station_info", description = "查询配送网点入柜配置、排除产品和末端配送规则")
  public StationInfo getStationInfo(
      @ToolParam(description = "运单号，例如 YT123456789") String waybillNo
  ) {
    return logisticsQueryService.getStationInfo(waybillNo);
  }
}
