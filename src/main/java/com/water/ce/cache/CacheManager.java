package com.water.ce.cache;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
public interface CacheManager {
    ShardedJedis getShardedJedis();

    ShardedJedisPool getShardedJedisPool();

    void set(String key, String value);

    Long sadd(String key, String... value);

    Long llen(String key);

    void del(String key);

    void lpush(String key, String... value);

    String lpop(String key);
    
    byte[] lpop(byte[] key);

    boolean sismember(String key, String value);

    byte[] getqueueheader(byte[] bytes);
}
