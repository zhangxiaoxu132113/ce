package com.water.crawl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.water.crawl")
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
