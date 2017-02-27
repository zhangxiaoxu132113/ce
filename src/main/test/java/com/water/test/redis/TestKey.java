package com.water.test.redis;


import com.water.crawl.core.cache.CacheManager;
import org.junit.Test;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * Created by zhangmiaojie on 2017/2/16.
 */
public class TestKey {
    @Resource
    private CacheManager cacheManager;

    @Test
    public void delKey() {
        ShardedJedis redis = cacheManager.getShardedJedis();
        String username = redis.get("username");
        System.out.println(username);
    }
}
