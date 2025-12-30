package com.example.pi.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PiCalculationRequest {
    
    @Min(value = 1, message = "The min iterations should be 1")
    @Max(value = 100_000_000, message = "The max interations should not exceed 100,000,000")
    private long iterations = 1_000_000;
    
    private boolean parallel = false;

    public long getIterations() {
        return iterations;
    }

    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }
}
