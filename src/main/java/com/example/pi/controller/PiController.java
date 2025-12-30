package com.example.pi.controller;

import com.example.pi.dto.PiCalculationRequest;
import com.example.pi.dto.PiCalculationResponse;
import com.example.pi.service.PiCalculatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api")
@Validated
public class PiController {

    private final PiCalculatorService piCalculatorService;
    private static final long MAX_ITERATIONS = 100_000_000L;
    private static final long DEFAULT_ITERATIONS = 1_000_000L;

    public PiController(PiCalculatorService piCalculatorService) {
        this.piCalculatorService = piCalculatorService;
    }

    @GetMapping("/calculate-pi")
    public ResponseEntity<PiCalculationResponse> calculatePi(
            @RequestParam(defaultValue = "1000000") 
            @Min(value = 1, message = "Iterations must be at least 1")
            @Max(value = MAX_ITERATIONS, message = "Iterations cannot exceed " + MAX_ITERATIONS)
            long iterations,
            @RequestParam(defaultValue = "false") boolean parallel) {
        
        long startTime = System.nanoTime();
        double piValue = parallel ? 
            piCalculatorService.calculatePiParallel(iterations) : 
            piCalculatorService.calculatePi(iterations);
        long endTime = System.nanoTime();
        
        PiCalculationResponse response = PiCalculationResponse.builder()
            .iterations(iterations)
            .piValue(piValue)
            .actualPi(Math.PI)
            .error(Math.abs(piValue - Math.PI))
            .errorPercentage(Math.abs(piValue - Math.PI) / Math.PI * 100)
            .executionTimeMs((endTime - startTime) / 1_000_000.0)
            .parallel(parallel)
            .availableProcessors(Runtime.getRuntime().availableProcessors())
            .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-pi")
    public ResponseEntity<PiCalculationResponse> calculatePiPost(@Valid @RequestBody PiCalculationRequest request) {
        long startTime = System.nanoTime();
        double piValue = request.isParallel() ? 
            piCalculatorService.calculatePiParallel(request.getIterations()) : 
            piCalculatorService.calculatePi(request.getIterations());
        long endTime = System.nanoTime();
        
        PiCalculationResponse response = PiCalculationResponse.builder()
            .iterations(request.getIterations())
            .piValue(piValue)
            .actualPi(Math.PI)
            .error(Math.abs(piValue - Math.PI))
            .errorPercentage(Math.abs(piValue - Math.PI) / Math.PI * 100)
            .executionTimeMs((endTime - startTime) / 1_000_000.0)
            .parallel(request.isParallel())
            .availableProcessors(Runtime.getRuntime().availableProcessors())
            .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Pi Calculator Service is running");
    }
}
