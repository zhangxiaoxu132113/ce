package com.water.crawl.db.web.config;

import com.water.crawl.db.web.filter.AllowOriginFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zhangmiaojie on 2017/2/27.
 */
@Configuration
//@ImportResource("applicationContext.xml")
public class WebConfigurer {

    @Bean
    public FilterRegistrationBean indexFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new AllowOriginFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}