# Pi Calculator API 使用示例

## 基础使用

### 1. 默认计算（100万次迭代，单线程）
```bash
curl http://localhost:8090/api/calculate-pi
```

### 2. 自定义迭代次数
```bash
curl "http://localhost:8090/api/calculate-pi?iterations=5000000"
```

### 3. 并行计算（推荐用于大量迭代）
```bash
curl "http://localhost:8090/api/calculate-pi?iterations=50000000&parallel=true"
```

## POST 请求示例

### 使用 JSON 请求体
```bash
curl -X POST http://localhost:8090/api/calculate-pi \
  -H "Content-Type: application/json" \
  -d '{
    "iterations": 10000000,
    "parallel": true
  }'
```

## 健康检查

### 简单健康检查
```bash
curl http://localhost:8090/api/health
```

## 错误处理示例

### 迭代次数过小
```bash
curl "http://localhost:8090/api/calculate-pi?iterations=0"
```
响应：
```json
{
  "status": 400,
  "error": "参数验证失败",
  "message": "calculatePi.iterations: Iterations must be at least 1"
}
```

### 迭代次数过大
```bash
curl "http://localhost:8090/api/calculate-pi?iterations=200000000"
```
响应：
```json
{
  "status": 400,
  "error": "参数验证失败",
  "message": "calculatePi.iterations: Iterations cannot exceed 100000000"
}
```

## 性能对比测试

### 单线程 vs 并行（1000万次迭代）
```bash
# 单线程
time curl "http://localhost:8090/api/calculate-pi?iterations=10000000&parallel=false"

# 并行
time curl "http://localhost:8090/api/calculate-pi?iterations=10000000&parallel=true"
```

## 响应示例

```json
{
  "iterations": 10000000,
  "piValue": 3.1416084,
  "actualPi": 3.141592653589793,
  "error": 0.000015746410207,
  "errorPercentage": 0.0005013,
  "executionTimeMs": 156.8,
  "parallel": true,
  "availableProcessors": 8
}
```
