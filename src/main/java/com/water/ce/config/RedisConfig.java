package com.water.ce.config;

import com.water.ce.cache.CacheManager;
import com.water.ce.cache.RedisCacheManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@Configuration
public class RedisConfig {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Bean(name = "redisCacheManager")
    public CacheManager getCacheManager() {
        return new RedisCacheManagerImpl(shardedJedisPool);
    }

}
