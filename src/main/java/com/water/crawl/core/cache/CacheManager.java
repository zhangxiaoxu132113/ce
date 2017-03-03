package com.water.crawl.core.cache;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
public interface CacheManager {
    public ShardedJedis getShardedJedis();
}
