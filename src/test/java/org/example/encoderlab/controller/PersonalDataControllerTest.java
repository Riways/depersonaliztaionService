package org.example.encoderlab.controller;

import org.example.encoderlab.dto.PersonalDataResponse;
import org.example.encoderlab.service.PersonalDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonalDataController.class)
class PersonalDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonalDataService pds;


    @Test
    void extract_shouldReturn200() throws Exception {
        when(pds.process(anyString(), anyString()))
                .thenReturn(new PersonalDataResponse(
                        List.of("a@b.com"), List.of(), ""));

        mockMvc.perform(get("/api/v1/personal-data")
                        .param("text", "a@b.com")
                        .param("mode", "extract"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emails[0]").value("a@b.com"));
    }

    @Test
    void blankText_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/personal-data")
                        .param("text", "")
                        .param("mode", "extract"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidMode_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/personal-data")
                        .param("text", "test")
                        .param("mode", "xxx"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void serviceThrows_shouldReturn500() throws Exception {
        when(pds.process(anyString(), anyString()))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/api/v1/personal-data")
                        .param("text", "a@b.com")
                        .param("mode", "extract"))
                .andExpect(status().isInternalServerError());
    }
}