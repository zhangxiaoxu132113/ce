package com.water.crawl.core.cache;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
public class RedisCacheManagerImpl implements CacheManager {

    private ShardedJedisPool shardedJedisPool;

    public RedisCacheManagerImpl() {

    }
    public RedisCacheManagerImpl(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    @Override
    public ShardedJedis getShardedJedis() {
        if (shardedJedisPool == null) return null;
        return shardedJedisPool.getResource();
    }
}
