package com.example.pi.integration;

import com.example.pi.dto.PiCalculationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Pi Calculator Integration Tests")
class PiCalculatorIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    @DisplayName("Should calculate Pi via REST API with default parameters")
    void testCalculatePi_Integration_Default() {
        String url = getBaseUrl() + "/calculate-pi";
        
        ResponseEntity<PiCalculationResponse> response = 
            restTemplate.getForEntity(url, PiCalculationResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        PiCalculationResponse body = response.getBody();
        assertEquals(1_000_000, body.getIterations());
        assertFalse(body.isParallel());
        assertTrue(body.getPiValue() > 0);
        assertTrue(body.getError() >= 0);
        assertTrue(body.getExecutionTimeMs() >= 0);
    }

    @Test
    @DisplayName("Should calculate Pi with custom iterations")
    void testCalculatePi_Integration_CustomIterations() {
        String url = getBaseUrl() + "/calculate-pi?iterations=5000000";
        
        ResponseEntity<PiCalculationResponse> response = 
            restTemplate.getForEntity(url, PiCalculationResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5_000_000, response.getBody().getIterations());
    }

    @Test
    @DisplayName("Should calculate Pi in parallel mode")
    void testCalculatePi_Integration_Parallel() {
        String url = getBaseUrl() + "/calculate-pi?iterations=1000000&parallel=true";
        
        ResponseEntity<PiCalculationResponse> response = 
            restTemplate.getForEntity(url, PiCalculationResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isParallel());
        assertTrue(response.getBody().getAvailableProcessors() > 0);
    }

    @Test
    @DisplayName("Should return 400 for invalid iterations")
    void testCalculatePi_Integration_InvalidIterations() {
        String url = getBaseUrl() + "/calculate-pi?iterations=0";
        
        ResponseEntity<String> response = 
            restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 for iterations exceeding maximum")
    void testCalculatePi_Integration_ExceedMaxIterations() {
        String url = getBaseUrl() + "/calculate-pi?iterations=200000000";
        
        ResponseEntity<String> response = 
            restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Health endpoint should return OK")
    void testHealth_Integration() {
        String url = getBaseUrl() + "/health";
        
        ResponseEntity<String> response = 
            restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pi Calculator Service is running", response.getBody());
    }

    @Test
    @DisplayName("Should calculate Pi with reasonable accuracy")
    void testCalculatePi_Integration_Accuracy() {
        String url = getBaseUrl() + "/calculate-pi?iterations=10000000";
        
        ResponseEntity<PiCalculationResponse> response = 
            restTemplate.getForEntity(url, PiCalculationResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        PiCalculationResponse body = response.getBody();
        double errorPercentage = body.getErrorPercentage();
        
        // With 10 million iterations, error should typically be less than 0.1%
        assertTrue(errorPercentage < 1.0, 
            "Error percentage should be less than 1% with 10M iterations");
    }
}
