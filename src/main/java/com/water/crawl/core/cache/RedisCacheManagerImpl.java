package com.water.crawl.core.cache;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
public class RedisCacheManagerImpl implements CacheManager {

    private ShardedJedisPool pool;

    public RedisCacheManagerImpl() {

    }
    public RedisCacheManagerImpl(ShardedJedisPool shardedJedisPool) {
        this.pool = shardedJedisPool;
    }

    @Override
    public ShardedJedis getShardedJedis() {
        if (pool == null) return null;
        return pool.getResource();
    }

    @Override
    public ShardedJedisPool getShardedJedisPool() {
        return pool;
    }

    @Override
    public void set(String key, String value) {
        ShardedJedis jedis = this.pool.getResource();
        try {
            jedis.set(key, value);
            this.pool.returnResource(jedis);
        } catch (Exception e) {
            this.pool.returnBrokenResource(jedis);
        }
    }

    @Override
    public Long sadd(String key, String... value) {
        ShardedJedis jedis = this.pool.getResource();
        Long result = Long.valueOf(0L);
        try {
            result = jedis.sadd(key, value);
            this.pool.returnResource(jedis);
        } catch (Exception e) {
            this.pool.returnBrokenResource(jedis);
        }
        return result;
    }

    @Override
    public Long llen(String key) {
        ShardedJedis jedis = this.pool.getResource();
        Long result = Long.valueOf(-1L);
        try {
            result = jedis.llen(key);
            this.pool.returnResource(jedis);
        } catch (Exception e) {
            this.pool.returnBrokenResource(jedis);
        }
        return result;
    }

    @Override
    public void del(String key) {
        ShardedJedis jedis = this.pool.getResource();
        try {
            jedis.del(key);
            this.pool.returnResource(jedis);
        } catch (Exception e) {
            this.pool.returnBrokenResource(jedis);
        }
    }

    @Override
    public void lpush(String key, String... value) {
        ShardedJedis jedis = this.pool.getResource();
        try {
            jedis.lpush(key,value);
            this.pool.returnResource(jedis);
        } catch (Exception e) {
            this.pool.returnBrokenResource(jedis);
        }
    }

    @Override
    public String lpop(String key) {
        ShardedJedis jedis = this.pool.getResource();
        String result = "";
        try {
            result = jedis.lpop(key);
            this.pool.returnResource(jedis);
        } catch (Exception e) {
            this.pool.returnBrokenResource(jedis);
        }
        return result;
    }
}
