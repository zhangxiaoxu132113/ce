package com.water.crawl.core.config;

import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.core.cache.RedisCacheManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.cache.RedisCacheManager;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@Configuration
public class RedisConfig {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Bean(name = "redisCacheManager")
    @Scope("singleton")
    public CacheManager getCacheManager() {
        return new RedisCacheManagerImpl(shardedJedisPool);
    }

}
