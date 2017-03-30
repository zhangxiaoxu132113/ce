package com.water.crawl.utils;

/**
 * Created by zhangmiaojie on 2017/3/30.
 */
public class RedisKey {

    public interface Prefix {
        public static final String FETCHING_ROOT_URLS = "fetching_root_urls" + Version.VERSION_NO;
        public static final String FETCHing_ARTICLE_URLS = "fetching_article_urls" + Version.VERSION_NO;

        public static final String FETCHING_URL_RECORDS = "fetching_url_records" + Version.VERSION_NO;  //
    }

    public interface Version {
        public static final String VERSION_NO = "_V1.0";
    }
}
