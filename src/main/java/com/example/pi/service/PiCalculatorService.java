package com.example.pi.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

@Service
public class PiCalculatorService {

    /**
     * 使用蒙特卡洛方法计算π值（单线程版本）
     * 原理：在单位正方形内随机生成点，计算落在1/4圆内的点的比例
     * π ≈ 4 * (圆内点数 / 总点数)
     * 
     * @param iterations 迭代次数
     * @return 计算得到的π近似值
     */
    public double calculatePi(long iterations) {
        long insideCircle = 0;

        for (long i = 0; i < iterations; i++) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            
            // 判断点是否在1/4圆内（使用勾股定理）
            if (x * x + y * y <= 1.0) {
                insideCircle++;
            }
        }

        return 4.0 * insideCircle / iterations;
    }

    /**
     * 使用蒙特卡洛方法计算π值（多线程并行版本）
     * 利用 Java 并行流自动分配任务到多个 CPU 核心
     * 
     * @param iterations 迭代次数
     * @return 计算得到的π近似值
     */
    public double calculatePiParallel(long iterations) {
        long insideCircle = LongStream.range(0, iterations)
            .parallel()
            .filter(i -> {
                double x = ThreadLocalRandom.current().nextDouble();
                double y = ThreadLocalRandom.current().nextDouble();
                return x * x + y * y <= 1.0;
            })
            .count();

        return 4.0 * insideCircle / iterations;
    }
}
