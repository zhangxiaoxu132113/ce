package com.water.crawl.utils;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by mrwater on 2016/11/29.
 * 从配置文件读取的常量
 */
public class Constants {
    public static Properties properties;
    public static String ACCESS_LOG_PATH;
    public static String FILTER_STATIC_RESOURCE;
    public static String CRALWER_PATH;

    static {
        Resource resource = new ClassPathResource("/config.properties");
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
            CRALWER_PATH = (String) properties.get("crawler.path");
            ACCESS_LOG_PATH = (String) properties.get("access.log.path");
            FILTER_STATIC_RESOURCE = (String) properties.get("filter.static.resource");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
