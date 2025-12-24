# Pi Calculator - 蒙特卡洛方法计算π

基于 Java 11 和 Spring Boot 的π值计算器，使用蒙特卡洛方法。

## 项目说明

该项目提供 REST API 接口，允许用户指定迭代步骤数量来计算π的近似值。

### 蒙特卡洛方法原理

在单位正方形 [0,1] × [0,1] 内随机生成点，计算落在1/4圆（半径为1）内的点的比例：
- 正方形面积 = 1
- 1/4圆面积 = π/4
- π ≈ 4 × (圆内点数 / 总点数)

## 运行项目

```bash
mvn spring-boot:run
```

## API 接口

### 计算π值

**端点**: `GET /api/calculate-pi`

**参数**:
- `iterations` (可选): 迭代次数，默认值为 1000000

**示例请求**:
```bash
# 使用默认迭代次数
curl http://localhost:8080/api/calculate-pi

# 指定迭代次数
curl http://localhost:8080/api/calculate-pi?iterations=10000000
```

**响应示例**:
```json
{
  "iterations": 10000000,
  "piValue": 3.1415926,
  "actualPi": 3.141592653589793,
  "error": 0.0000000535897933,
  "executionTimeMs": 245
}
```

## 技术栈

- Java 11
- Spring Boot 2.7.18
- Maven

## 项目结构

```
├── pom.xml
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
