# Pi Calculator - Monte Carlo Method

A Spring Boot application (Java 11) that calculates π using the Monte Carlo method, with automated code review powered by Claude Code in GitLab CI/CD.

## Project Overview

This project provides a REST API that allows users to specify the number of iterations to calculate an approximate value of π using the Monte Carlo method. It includes both single-threaded and parallel (multi-core) computation options.

### Monte Carlo Method Principle

Random points are generated within a unit square [0,1] × [0,1], and the ratio of points falling inside a quarter circle (radius 1) is calculated:
- Square area = 1
- Quarter circle area = π/4
- π ≈ 4 × (points inside circle / total points)

## GitLab CI/CD with Claude Code Integration

This project demonstrates automated code review using Claude Code in GitLab CI pipelines.

### Pipeline Stages

1. **gendev** - Automated code review on merge requests
2. **build** - Compile the Java application
3. **test** - Run unit tests

### Claude Code Review Stage

The `gendev` stage automatically:
- Fetches code changes from merge requests
- Sends the diff to Claude Code for AI-powered review
- Posts review feedback as comments on the merge request

**Key Features:**
- Runs only on merge requests
- Uses AWS Bedrock with Claude Sonnet 4.5
- Provides code quality feedback, identifies potential issues, and suggests improvements
- Non-blocking (allows pipeline to continue even if review fails)

### Setup Requirements

**GitLab CI/CD Variables:**
Set these in your GitLab project settings (Settings → CI/CD → Variables):

- `GITLAB_TOKEN` - Personal access token with `api` scope (required to post MR comments)
- `AWS_BEARER_TOKEN_BEDROCK` - AWS credentials for Bedrock access

**GitLab Runner:**
- Requires Docker executor for running containers

### CI Configuration Highlights

```yaml
gendev:
  stage: gendev
  image: node:24-alpine3.21
  script:
    - git diff origin/$CI_MERGE_REQUEST_TARGET_BRANCH_NAME...HEAD > changes.diff
    - claude -p "Review the code changes in changes.diff file..."
    - Post review results to MR via GitLab API
  only:
    - merge_requests
```

## Running the Application

```bash
mvn spring-boot:run
```

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PiCalculatorServiceTest

# Run with coverage report
mvn test jacoco:report
```

See [TEST_GUIDE.md](TEST_GUIDE.md) for detailed testing documentation.

## API Endpoints

### Calculate Pi (GET)

**Endpoint**: `GET /api/calculate-pi`

**Parameters**:
- `iterations` (optional): Number of iterations, default 1,000,000, max 100,000,000
- `parallel` (optional): Use multi-core parallel computation, default `false`

**Example Requests**:
```bash
# Single-threaded with default iterations
curl http://localhost:8090/api/calculate-pi

# Parallel computation with 10 million iterations
curl "http://localhost:8090/api/calculate-pi?iterations=10000000&parallel=true"
```

**Response Example**:
```json
{
  "iterations": 10000000,
  "piValue": 3.1415926,
  "actualPi": 3.141592653589793,
  "error": 0.0000000535897933,
  "errorPercentage": 0.0000017,
  "executionTimeMs": 245.5,
  "parallel": true,
  "availableProcessors": 8
}
```

### Calculate Pi (POST)

**Endpoint**: `POST /api/calculate-pi`

**Request Body**:
```json
{
  "iterations": 10000000,
  "parallel": true
}
```

**Response**: Same as GET endpoint

### Health Check

**Endpoint**: `GET /api/health`

Returns service status.

### Actuator Endpoints

- `GET /actuator/health` - Detailed health information
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics


## Performance

The parallel implementation uses Java parallel streams to leverage multiple CPU cores:
- Typically 2-4x faster on multi-core systems
- Performance scales with available processor cores
- Uses `ThreadLocalRandom` for thread-safe random number generation

## Technology Stack

- Java 11
- Spring Boot 2.7.18
- Maven
- GitLab CI/CD
- Claude Code (via AWS Bedrock)

## Project Structure

```
├── .gitlab-ci.yml                    # CI/CD pipeline configuration
├── pom.xml                           # Maven dependencies
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/pi/
│       │       ├── PiCalculatorApplication.java
│       │       ├── controller/
│       │       │   └── PiController.java
│       │       └── service/
│       │           └── PiCalculatorService.java
│       └── resources/
│           └── application.properties
└── README.md
```

## Contributing

When creating merge requests, the Claude Code review will automatically run and provide feedback on your changes. Review the comments and address any suggestions before merging.
