package com.water.ce;

import com.water.ce.web.service.CSDNCrawlingArticleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.water")
public class Bootstrap implements EmbeddedServletContainerCustomizer{

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(8087);
    }
}
