package com.example.pi.dto;

public class PiCalculationResponse {
    private long iterations;
    private double piValue;
    private double actualPi;
    private double error;
    private double errorPercentage;
    private double executionTimeMs;
    private boolean parallel;
    private int availableProcessors;

    // Default constructor for Jackson
    public PiCalculationResponse() {
    }

    private PiCalculationResponse(Builder builder) {
        this.iterations = builder.iterations;
        this.piValue = builder.piValue;
        this.actualPi = builder.actualPi;
        this.error = builder.error;
        this.errorPercentage = builder.errorPercentage;
        this.executionTimeMs = builder.executionTimeMs;
        this.parallel = builder.parallel;
        this.availableProcessors = builder.availableProcessors;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public long getIterations() { return iterations; }
    public double getPiValue() { return piValue; }
    public double getActualPi() { return actualPi; }
    public double getError() { return error; }
    public double getErrorPercentage() { return errorPercentage; }
    public double getExecutionTimeMs() { return executionTimeMs; }
    public boolean isParallel() { return parallel; }
    public int getAvailableProcessors() { return availableProcessors; }

    // Setters for Jackson
    public void setIterations(long iterations) { this.iterations = iterations; }
    public void setPiValue(double piValue) { this.piValue = piValue; }
    public void setActualPi(double actualPi) { this.actualPi = actualPi; }
    public void setError(double error) { this.error = error; }
    public void setErrorPercentage(double errorPercentage) { this.errorPercentage = errorPercentage; }
    public void setExecutionTimeMs(double executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public void setParallel(boolean parallel) { this.parallel = parallel; }
    public void setAvailableProcessors(int availableProcessors) { this.availableProcessors = availableProcessors; }

    public static class Builder {
        private long iterations;
        private double piValue;
        private double actualPi;
        private double error;
        private double errorPercentage;
        private double executionTimeMs;
        private boolean parallel;
        private int availableProcessors;

        public Builder iterations(long iterations) {
            this.iterations = iterations;
            return this;
        }

        public Builder piValue(double piValue) {
            this.piValue = piValue;
            return this;
        }

        public Builder actualPi(double actualPi) {
            this.actualPi = actualPi;
            return this;
        }

        public Builder error(double error) {
            this.error = error;
            return this;
        }

        public Builder errorPercentage(double errorPercentage) {
            this.errorPercentage = errorPercentage;
            return this;
        }

        public Builder executionTimeMs(double executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        public Builder parallel(boolean parallel) {
            this.parallel = parallel;
            return this;
        }

        public Builder availableProcessors(int availableProcessors) {
            this.availableProcessors = availableProcessors;
            return this;
        }

        public PiCalculationResponse build() {
            return new PiCalculationResponse(this);
        }
    }
}
