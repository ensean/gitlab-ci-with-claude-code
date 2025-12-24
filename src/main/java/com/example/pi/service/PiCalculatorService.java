package com.example.pi.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PiCalculatorService {

    /**
     * 使用蒙特卡洛方法计算π值
     * 原理：在单位正方形内随机生成点，计算落在1/4圆内的点的比例
     * π ≈ 4 * (圆内点数 / 总点数)
     */
    public double calculatePi(long iterations) {
        Random random = new Random();
        long insideCircle = 0;

        for (long i = 0; i < iterations; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            
            // 判断点是否在1/4圆内
            if (x * x + y * y <= 1.0) {
                insideCircle++;
            }
        }

        return 4.0 * insideCircle / iterations;
    }
}
