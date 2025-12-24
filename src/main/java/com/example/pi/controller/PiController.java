package com.example.pi.controller;

import com.example.pi.service.PiCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PiController {

    @Autowired
    private PiCalculatorService piCalculatorService;

    @GetMapping("/calculate-pi")
    public Map<String, Object> calculatePi(
            @RequestParam(defaultValue = "1000000") long iterations) {
        
        long startTime = System.currentTimeMillis();
        double piValue = piCalculatorService.calculatePi(iterations);
        long endTime = System.currentTimeMillis();
        
        Map<String, Object> response = new HashMap<>();
        response.put("iterations", iterations);
        response.put("piValue", piValue);
        response.put("actualPi", Math.PI);
        response.put("error", Math.abs(piValue - Math.PI));
        response.put("executionTimeMs", endTime - startTime);
        
        return response;
    }
}
