package com.logistics.agent.admin.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.logistics.agent.admin.AgentAdminApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = AgentAdminApplication.class)
@AutoConfigureMockMvc
class IntentRecognitionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldRecognizeDesignatedLocation() throws Exception {
    mockMvc.perform(post("/api/v1/intent/recognize")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "waybillNo": "YT123456789",
                  "callId": "CALL-001",
                  "transcript": "我白天不在家，把快递放小区门卫"
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tagCode").value("Racking"))
        .andExpect(jsonPath("$.value").value("3"))
        .andExpect(jsonPath("$.intentName").value("指定地点"))
        .andExpect(jsonPath("$.contextSentence").value("我白天不在家，把快递放小区门卫"))
        .andExpect(jsonPath("$.extracted.locationDetail").value("小区门卫"))
        .andExpect(jsonPath("$.needHumanReview").value(false));
  }

  @Test
  void shouldRejectBlankTranscript() throws Exception {
    mockMvc.perform(post("/api/v1/intent/recognize")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "waybillNo": "YT123456789",
                  "callId": "CALL-001",
                  "transcript": ""
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
  }
}
