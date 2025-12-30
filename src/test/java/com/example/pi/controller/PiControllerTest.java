package com.example.pi.controller;

import com.example.pi.service.PiCalculatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PiController.class)
@DisplayName("Pi Controller Tests")
class PiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PiCalculatorService piCalculatorService;

    @Test
    @DisplayName("GET /api/calculate-pi should return pi calculation with default parameters")
    void testCalculatePi_DefaultParameters() throws Exception {
        when(piCalculatorService.calculatePi(anyLong())).thenReturn(3.14159);

        mockMvc.perform(get("/api/calculate-pi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iterations").value(1000000))
                .andExpect(jsonPath("$.piValue").value(3.14159))
                .andExpect(jsonPath("$.actualPi").value(Math.PI))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errorPercentage").exists())
                .andExpect(jsonPath("$.executionTimeMs").exists())
                .andExpect(jsonPath("$.parallel").value(false))
                .andExpect(jsonPath("$.availableProcessors").exists());
    }

    @Test
    @DisplayName("GET /api/calculate-pi should accept custom iterations")
    void testCalculatePi_CustomIterations() throws Exception {
        when(piCalculatorService.calculatePi(5000000L)).thenReturn(3.14159);

        mockMvc.perform(get("/api/calculate-pi")
                        .param("iterations", "5000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iterations").value(5000000))
                .andExpect(jsonPath("$.parallel").value(false));
    }

    @Test
    @DisplayName("GET /api/calculate-pi should use parallel calculation when requested")
    void testCalculatePi_ParallelMode() throws Exception {
        when(piCalculatorService.calculatePiParallel(anyLong())).thenReturn(3.14159);

        mockMvc.perform(get("/api/calculate-pi")
                        .param("iterations", "1000000")
                        .param("parallel", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parallel").value(true));
    }

    @Test
    @DisplayName("GET /api/calculate-pi should reject iterations less than 1")
    void testCalculatePi_InvalidIterationsTooSmall() throws Exception {
        mockMvc.perform(get("/api/calculate-pi")
                        .param("iterations", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/calculate-pi should reject iterations exceeding maximum")
    void testCalculatePi_InvalidIterationsTooLarge() throws Exception {
        mockMvc.perform(get("/api/calculate-pi")
                        .param("iterations", "200000000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/calculate-pi should accept JSON request body")
    void testCalculatePiPost_ValidRequest() throws Exception {
        when(piCalculatorService.calculatePiParallel(10000000L)).thenReturn(3.14159);

        String requestBody = "{\"iterations\": 10000000, \"parallel\": true}";

        mockMvc.perform(post("/api/calculate-pi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iterations").value(10000000))
                .andExpect(jsonPath("$.parallel").value(true))
                .andExpect(jsonPath("$.piValue").value(3.14159));
    }

    @Test
    @DisplayName("POST /api/calculate-pi should use default values when not specified")
    void testCalculatePiPost_DefaultValues() throws Exception {
        when(piCalculatorService.calculatePi(1000000L)).thenReturn(3.14159);

        String requestBody = "{}";

        mockMvc.perform(post("/api/calculate-pi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iterations").value(1000000))
                .andExpect(jsonPath("$.parallel").value(false));
    }

    @Test
    @DisplayName("POST /api/calculate-pi should reject invalid iterations in request body")
    void testCalculatePiPost_InvalidIterations() throws Exception {
        String requestBody = "{\"iterations\": 0, \"parallel\": false}";

        mockMvc.perform(post("/api/calculate-pi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("GET /api/health should return service status")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Pi Calculator Service is running"));
    }

    @Test
    @DisplayName("Should calculate error percentage correctly")
    void testCalculatePi_ErrorPercentage() throws Exception {
        when(piCalculatorService.calculatePi(anyLong())).thenReturn(3.14);

        mockMvc.perform(get("/api/calculate-pi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorPercentage").isNumber());
    }

    @Test
    @DisplayName("Should return execution time in milliseconds")
    void testCalculatePi_ExecutionTime() throws Exception {
        when(piCalculatorService.calculatePi(anyLong())).thenReturn(3.14159);

        mockMvc.perform(get("/api/calculate-pi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.executionTimeMs").isNumber());
    }
}
