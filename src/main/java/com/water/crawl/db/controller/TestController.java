package com.water.crawl.db.controller;

import com.water.crawl.core.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@RestController
public class TestController {

    @Resource
    private CacheManager cacheManager;

    @RequestMapping(value = "/test")
    public String test() {
        ShardedJedis redis = cacheManager.getShardedJedis();
        String username = redis.get("mrwater");
        System.out.println("username = " + username);
        return "HelleWorld";
    }
}
