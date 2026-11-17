package com.logistics.agent.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MockLogisticsQueryServiceTest {

  private final MockLogisticsQueryService service = new MockLogisticsQueryService();

  @Test
  void shouldReturnConsistentDemoBusinessContext() {
    String waybillNo = MockLogisticsQueryService.DEMO_WAYBILL_NO;

    assertThat(service.getWaybillInfo(waybillNo).status()).isEqualTo("DELIVERING");
    assertThat(service.getTrackingInfo(waybillNo).events()).hasSize(2);
    assertThat(service.getCustomerPreference(waybillNo).lastRackingValue()).isEqualTo("3");
    assertThat(service.getStationInfo(waybillNo).deliveryPolicies()).hasSize(2);
  }

  @Test
  void shouldRejectUnknownWaybill() {
    assertThatThrownBy(() -> service.getWaybillInfo("UNKNOWN"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("UNKNOWN");
  }
}
