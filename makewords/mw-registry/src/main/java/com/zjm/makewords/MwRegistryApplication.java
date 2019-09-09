package com.zjm.makewords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/2 0002 13:05
 */
@EnableEurekaServer
@SpringBootApplication
public class MwRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(MwRegistryApplication.class);
    }
}
