package com.water.crawl.db.controller;

import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.db.service.article.ICSDNArticleService;
import com.water.crawl.work.FetchArticleUrlCrawlTask;
import com.water.es.api.Service.IArticleService;
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
    private ICSDNArticleService icsdnArticleService;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    @Resource
    private CacheManager cacheManager;

    @RequestMapping(value = "/test")
    public String test() {
//        new Thread(new FetchArticleUrlCrawlTask(cacheManager)).start();
        new FetchArticleUrlCrawlTask(cacheManager).start();
        return "test";
    }

    @RequestMapping(value = "/test1")
    public String test1() {
        ShardedJedis jedis = cacheManager.getShardedJedis();
        jedis.set("username", "haha");
        return "success!";
    }
}
