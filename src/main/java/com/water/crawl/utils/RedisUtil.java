package com.water.crawl.utils;

import com.water.crawl.core.cache.CacheManager;
import redis.clients.jedis.ShardedJedis;

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
