package com.medicore.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicoreServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicoreServerApplication.class, args);
    }
}