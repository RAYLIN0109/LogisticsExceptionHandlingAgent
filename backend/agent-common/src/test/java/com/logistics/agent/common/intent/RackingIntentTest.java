package com.logistics.agent.common.intent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RackingIntentTest {

  @Test
  void shouldCoverAllEightBusinessValues() {
    assertThat(RackingIntent.values()).hasSize(8);
    assertThat(RackingIntent.fromValue("2")).isEqualTo(RackingIntent.IN_STATION);
    assertThat(RackingIntent.fromValue("3").intentName()).isEqualTo("指定地点");
    assertThat(RackingIntent.fromValue("8")).isEqualTo(RackingIntent.UNRELATED);
  }

  @Test
  void shouldRejectUnknownValue() {
    assertThatThrownBy(() -> RackingIntent.fromValue("9"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("9");
  }

  @Test
  void shouldKeepOriginalCheckTypeMapping() {
    assertThat(CheckTypeAction.fromRackingValue("2")).isEqualTo(CheckTypeAction.IN_STATION);
    assertThat(CheckTypeAction.fromRackingValue("1")).isEqualTo(CheckTypeAction.RESET_CHECK);
    assertThat(CheckTypeAction.fromRackingValue("3")).isEqualTo(CheckTypeAction.RESET_CHECK);
    assertThat(CheckTypeAction.fromRackingValue("8")).isEqualTo(CheckTypeAction.RESET_CHECK);
  }
}
