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
    private static String ACCESS_LOG_PATH = "access.log.path";
    private static String FILTER_STATIC_RESOURCE = "filter.static.resource";

    static {
        Resource resource = new ClassPathResource("/crawl/config.properties");
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getACCESS_LOG_PATH() {
        return (String) properties.get(ACCESS_LOG_PATH);
    }

    public static String getFILTER_STATIC_RESOURCE() {
        return (String) properties.get(FILTER_STATIC_RESOURCE);
    }
}
