package com.opositores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OpositoresApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpositoresApplication.class, args);
    }
}
