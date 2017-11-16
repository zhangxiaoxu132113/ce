package com.water.ce.utils;


import com.water.ce.cache.CacheManager;

/**
 * Created by zhangmiaojie on 2017/3/31.
 */
public class RedisUtil {
    private static CacheManager cacheManager;

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public static void set() {
//        ShardedJedis cacheManager.getShardedJedis();
    }
}
