package com.example.pi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Application Context Tests")
class PiCalculatorApplicationTest {

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        // This test verifies that the Spring application context loads without errors
    }
}
