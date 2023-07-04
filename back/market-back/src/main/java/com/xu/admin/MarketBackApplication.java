package com.xu.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author AITIAN
 */
@EnableAsync
@ComponentScan("com.xu")
@EnableTransactionManagement
@SpringBootApplication
public class MarketBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketBackApplication.class, args);
    }

}
