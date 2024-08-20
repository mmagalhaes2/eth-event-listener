package com.math.cleanarchex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CleanArchExApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleanArchExApplication.class, args);
    }

}
