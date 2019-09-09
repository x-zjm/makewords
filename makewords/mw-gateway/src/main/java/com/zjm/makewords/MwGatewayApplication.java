package com.zjm.makewords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @description:
 * @author: zjm
 * @time: 2019/9/2 0002 12:52
 */
@EnableZuulProxy
@SpringBootApplication
public class MwGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MwGatewayApplication.class);
    }
}
