package com.water.ce.utils;

/**
 * Created by zhangmiaojie on 2017/3/30.
 */
public class RedisKey {

    public interface Prefix {
        public static final String FETCHING_ROOT_URLS = "fetching_root_urls" + Version.VERSION_NO;       //待抓取的根url连接
        public static final String FETCHing_ARTICLE_URLS = "fetching_article_urls" + Version.VERSION_NO; //待抓取的文章连接
        public static final String FETCHING_URL_RECORDS = "visited_url_records" + Version.VERSION_NO;    //已经访问过的url连接
    }

    public interface Version {
        public static final String VERSION_NO = "_V1.0";
    }
}
