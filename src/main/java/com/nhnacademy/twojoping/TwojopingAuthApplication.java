package com.nhnacademy.twojoping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TwojopingAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwojopingAuthApplication.class, args);
    }

}
