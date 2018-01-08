package com.water.ce.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.Properties;

/**
 * Created by mrwater on 2016/11/28.
 */
public class Config {
    public static Configuration configuration;

    public static String craw_img_path;
    public static Properties properties;
    static {
        try {
            configuration = new PropertiesConfiguration("crawl/config.properties");
            load();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
    private Config() {}
    public static void load() {
        craw_img_path = configuration.getString("crawl.img.path");
    }

}
