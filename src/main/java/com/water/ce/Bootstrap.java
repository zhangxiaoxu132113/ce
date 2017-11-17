package com.water.ce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangmiaojie on 2017/2/4.
 * Port 8082
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.water.ce")
public class Bootstrap implements EmbeddedServletContainerCustomizer{

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(8087);
    }
}
