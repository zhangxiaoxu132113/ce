package com.water.crawl.core.cache;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
public interface CacheManager {
    public ShardedJedis getShardedJedis();

    public ShardedJedisPool getShardedJedisPool();

    public void set(String key, String value);

    public Long sadd(String key, String... value);

    public Long llen(String key);

    public void del(String key);

    public void lpush(String key, String... value);

    public String lpop(String key);

}
