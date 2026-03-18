package com.dit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.dit.*.persistence.mapper")
public class AiCodeTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeTestApplication.class, args);
    }
}
