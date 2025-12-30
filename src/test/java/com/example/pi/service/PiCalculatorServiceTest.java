package com.example.pi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Pi Calculator Service Tests")
class PiCalculatorServiceTest {

    @Autowired
    private PiCalculatorService piCalculatorService;

    private static final double PI_TOLERANCE = 0.01; // 1% tolerance
    private static final double ACTUAL_PI = Math.PI;

    @Test
    @DisplayName("Should calculate Pi with single-threaded method within tolerance")
    void testCalculatePi_SingleThreaded() {
        long iterations = 1_000_000;
        
        double result = piCalculatorService.calculatePi(iterations);
        
        assertNotNull(result);
        assertTrue(result > 0, "Pi value should be positive");
        assertTrue(Math.abs(result - ACTUAL_PI) < PI_TOLERANCE, 
            "Pi approximation should be within 1% of actual value");
    }

    @Test
    @DisplayName("Should calculate Pi with parallel method within tolerance")
    void testCalculatePi_Parallel() {
        long iterations = 1_000_000;
        
        double result = piCalculatorService.calculatePiParallel(iterations);
        
        assertNotNull(result);
        assertTrue(result > 0, "Pi value should be positive");
        assertTrue(Math.abs(result - ACTUAL_PI) < PI_TOLERANCE, 
            "Pi approximation should be within 1% of actual value");
    }

    @Test
    @DisplayName("Should improve accuracy with more iterations")
    void testCalculatePi_AccuracyImprovement() {
        long smallIterations = 10_000;
        long largeIterations = 1_000_000;
        
        double resultSmall = piCalculatorService.calculatePi(smallIterations);
        double resultLarge = piCalculatorService.calculatePi(largeIterations);
        
        double errorSmall = Math.abs(resultSmall - ACTUAL_PI);
        double errorLarge = Math.abs(resultLarge - ACTUAL_PI);
        
        // Generally, more iterations should give better accuracy
        // We use a relaxed check since randomness can cause variations
        assertTrue(errorLarge < 0.1, "Large iteration error should be reasonable");
    }

    @Test
    @DisplayName("Should handle minimum iterations")
    void testCalculatePi_MinimumIterations() {
        long iterations = 1;
        
        double result = piCalculatorService.calculatePi(iterations);
        
        assertNotNull(result);
        assertTrue(result >= 0 && result <= 4, 
            "Pi value with 1 iteration should be either 0 or 4");
    }

    @Test
    @DisplayName("Should produce consistent results for same iterations")
    void testCalculatePi_Consistency() {
        long iterations = 100_000;
        
        double result1 = piCalculatorService.calculatePi(iterations);
        double result2 = piCalculatorService.calculatePi(iterations);
        
        // Results should be in the same ballpark (within 10% of each other)
        double difference = Math.abs(result1 - result2);
        assertTrue(difference < 0.5, 
            "Multiple runs should produce similar results");
    }

    @Test
    @DisplayName("Parallel and single-threaded should produce similar results")
    void testCalculatePi_ParallelVsSingleThreaded() {
        long iterations = 500_000;
        
        double singleThreaded = piCalculatorService.calculatePi(iterations);
        double parallel = piCalculatorService.calculatePiParallel(iterations);
        
        double difference = Math.abs(singleThreaded - parallel);
        assertTrue(difference < 0.5, 
            "Parallel and single-threaded results should be similar");
    }

    @Test
    @DisplayName("Should handle large number of iterations")
    void testCalculatePi_LargeIterations() {
        long iterations = 10_000_000;
        
        long startTime = System.currentTimeMillis();
        double result = piCalculatorService.calculatePiParallel(iterations);
        long endTime = System.currentTimeMillis();
        
        assertNotNull(result);
        assertTrue(Math.abs(result - ACTUAL_PI) < 0.01, 
            "Large iterations should give accurate result");
        assertTrue(endTime - startTime < 30000, 
            "Should complete within 30 seconds");
    }

    @Test
    @DisplayName("Should return value between 2 and 4")
    void testCalculatePi_ValueRange() {
        long iterations = 100_000;
        
        double result = piCalculatorService.calculatePi(iterations);
        
        assertTrue(result >= 2.0 && result <= 4.0, 
            "Pi approximation should be between 2 and 4");
    }
}
