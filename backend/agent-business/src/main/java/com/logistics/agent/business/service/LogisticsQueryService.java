package com.logistics.agent.business.service;

import com.logistics.agent.business.model.CustomerPreference;
import com.logistics.agent.business.model.StationInfo;
import com.logistics.agent.business.model.TrackingInfo;
import com.logistics.agent.business.model.WaybillInfo;

public interface LogisticsQueryService {

  WaybillInfo getWaybillInfo(String waybillNo);

  TrackingInfo getTrackingInfo(String waybillNo);

  CustomerPreference getCustomerPreference(String waybillNo);

  StationInfo getStationInfo(String waybillNo);
}
