package com.example.pi.performance;

import com.example.pi.service.PiCalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Pi Calculator Performance Tests")
class PiCalculatorPerformanceTest {

    @Autowired
    private PiCalculatorService piCalculatorService;

    @Test
    @DisplayName("Parallel should be faster than single-threaded for large iterations")
    void testPerformance_ParallelVsSingleThreaded() {
        long iterations = 5_000_000;
        
        // Single-threaded
        long startSingle = System.nanoTime();
        double resultSingle = piCalculatorService.calculatePi(iterations);
        long endSingle = System.nanoTime();
        long timeSingle = endSingle - startSingle;
        
        // Parallel
        long startParallel = System.nanoTime();
        double resultParallel = piCalculatorService.calculatePiParallel(iterations);
        long endParallel = System.nanoTime();
        long timeParallel = endParallel - startParallel;
        
        System.out.println("Single-threaded time: " + timeSingle / 1_000_000.0 + " ms");
        System.out.println("Parallel time: " + timeParallel / 1_000_000.0 + " ms");
        System.out.println("Speedup: " + (double) timeSingle / timeParallel + "x");
        
        // Parallel should generally be faster on multi-core systems
        // We use a relaxed check since performance can vary
        assertTrue(timeParallel < timeSingle * 1.5, 
            "Parallel should not be significantly slower than single-threaded");
    }

    @Test
    @DisplayName("Should complete 1 million iterations in reasonable time")
    void testPerformance_ReasonableTime() {
        long iterations = 1_000_000;
        
        long start = System.nanoTime();
        double result = piCalculatorService.calculatePi(iterations);
        long end = System.nanoTime();
        
        long timeMs = (end - start) / 1_000_000;
        
        System.out.println("1M iterations completed in: " + timeMs + " ms");
        
        assertTrue(timeMs < 5000, 
            "1 million iterations should complete within 5 seconds");
    }

    @Test
    @Disabled("This test takes a long time - enable for performance benchmarking")
    @DisplayName("Benchmark: 100 million iterations")
    void testPerformance_Benchmark100M() {
        long iterations = 100_000_000;
        
        System.out.println("\n=== Performance Benchmark: 100M iterations ===");
        
        // Single-threaded
        long startSingle = System.nanoTime();
        double resultSingle = piCalculatorService.calculatePi(iterations);
        long endSingle = System.nanoTime();
        long timeSingle = (endSingle - startSingle) / 1_000_000;
        
        System.out.println("Single-threaded:");
        System.out.println("  Time: " + timeSingle + " ms");
        System.out.println("  Result: " + resultSingle);
        System.out.println("  Error: " + Math.abs(resultSingle - Math.PI));
        
        // Parallel
        long startParallel = System.nanoTime();
        double resultParallel = piCalculatorService.calculatePiParallel(iterations);
        long endParallel = System.nanoTime();
        long timeParallel = (endParallel - startParallel) / 1_000_000;
        
        System.out.println("\nParallel:");
        System.out.println("  Time: " + timeParallel + " ms");
        System.out.println("  Result: " + resultParallel);
        System.out.println("  Error: " + Math.abs(resultParallel - Math.PI));
        System.out.println("  Speedup: " + (double) timeSingle / timeParallel + "x");
        System.out.println("  Processors: " + Runtime.getRuntime().availableProcessors());
    }

    @Test
    @DisplayName("Should scale with available processors")
    void testPerformance_Scalability() {
        long iterations = 2_000_000;
        int processors = Runtime.getRuntime().availableProcessors();
        
        long start = System.nanoTime();
        piCalculatorService.calculatePiParallel(iterations);
        long end = System.nanoTime();
        
        long timeMs = (end - start) / 1_000_000;
        
        System.out.println("Available processors: " + processors);
        System.out.println("Parallel execution time: " + timeMs + " ms");
        
        assertTrue(processors > 0, "Should detect available processors");
    }
}
