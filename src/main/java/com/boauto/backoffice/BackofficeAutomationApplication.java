package com.boauto.backoffice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.boauto.backoffice")
@SpringBootApplication
public class BackofficeAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackofficeAutomationApplication.class, args);
    }
}
