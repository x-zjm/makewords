package com.zjm.makewords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/2 0002 16:58
 */
@EnableEurekaClient
@SpringBootApplication
public class MwWordApplication {
    public static void main(String[] args) {
        SpringApplication.run(MwWordApplication.class);
    }
}
