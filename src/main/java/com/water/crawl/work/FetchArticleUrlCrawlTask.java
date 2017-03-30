package com.water.crawl.work;

import com.water.crawl.core.WaterHtmlParse;
import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.utils.HttpRequestTool;
import com.water.crawl.utils.RedisKey;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangmiaojie on 2017/3/30.
 */
public class FetchArticleUrlCrawlTask implements Runnable {
    public final static String ROOT_URL = "http://www.csdn.net/";
    @Resource
    private CacheManager cacheManager;

    public FetchArticleUrlCrawlTask(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        initRootUrlQueue(false);
    }

    /**
     * 初始化，将第一个root url放到队列中
     *
     * @param isEmpty 是否清空数据
     */
    public void initRootUrlQueue(boolean isEmpty) {
        ShardedJedis jedis = cacheManager.getShardedJedis();
        if (isEmpty) {
            jedis.del(RedisKey.Prefix.FETCHING_ROOT_URLS);
        }
        pushRootUrl(ROOT_URL, RedisKey.Prefix.FETCHING_ROOT_URLS);
    }

    /**
     * 将跟节点放到队列
     *
     * @param links 跟节点
     */
    public void pushRootUrlQueue(List<String> links) {
        ShardedJedis jedis = cacheManager.getShardedJedis();
        if (jedis.llen(RedisKey.Prefix.FETCHING_ROOT_URLS) < 100000) {
            for (String link : links) {
                pushRootUrl(link, RedisKey.Prefix.FETCHING_ROOT_URLS);
            }
        }
    }

    /**
     * 校验url没有重复，将url放到给定的队列
     *
     * @param url       连接
     * @param queueName 队列名称
     */
    public void pushRootUrl(String url, String queueName) {
        ShardedJedis jedis = cacheManager.getShardedJedis();
        if (!jedis.sismember(RedisKey.Prefix.FETCHING_URL_RECORDS, url)) {
            jedis.lpush(queueName, url);
            jedis.sadd(RedisKey.Prefix.FETCHING_URL_RECORDS, url);
        }
    }

    /**
     * 解析过滤url，将文章的链接放到队列
     * http://blog.csdn.net/pelick/article/details/8331798
     */
    public void parseUrlAndPutQueue(String url) {
        if (StringUtils.isNotBlank(url)) {
            if (url.startsWith("http://blog.csdn.net/")) {
                pushRootUrl(url, RedisKey.Prefix.FETCHing_ARTICLE_URLS);
            }
        }
    }

    @Override
    public void run() {
        ShardedJedis jedis = cacheManager.getShardedJedis();
        String rootUrl;
        String pageHtml;
        while (jedis.llen(RedisKey.Prefix.FETCHING_ROOT_URLS) > 0) {
            //访问root
            rootUrl = jedis.lpop(RedisKey.Prefix.FETCHING_ROOT_URLS);
            if (StringUtils.isBlank(rootUrl)) continue;
            pageHtml = (String) HttpRequestTool.getRequest(rootUrl, false);
            List<String> links = WaterHtmlParse.getHrefWithA(pageHtml);
            for (String link_1 : links) {// 获取root下面说的所有的url连接 -- 第一层
                parseUrlAndPutQueue(link_1);
                pageHtml = (String) HttpRequestTool.getRequest(link_1);
                List<String> linksLevelTwo = WaterHtmlParse.getHrefWithA(pageHtml);
                for (String link_2 : linksLevelTwo) {// 遍历第一层的连接，开始抓取第二层的url连接 -- 第二层
                    parseUrlAndPutQueue(link_2);
                    pageHtml = (String) HttpRequestTool.getRequest(link_2);
                    List<String> linksLevelThree = WaterHtmlParse.getHrefWithA(pageHtml);
                    for (String link_3 : linksLevelThree) {//遍历第二层的连接，开始抓取第三层的url连接 -- 第三层
                        parseUrlAndPutQueue(link_3);
                        pageHtml = (String) HttpRequestTool.getRequest(link_3);
                        List<String> linksLevelFour = WaterHtmlParse.getHrefWithA(pageHtml);
                        for (String link_4 : linksLevelFour) {//遍历第三层的连接，将四层的url连接放到一个根url节点的队列中 -- 终止循环
                            parseUrlAndPutQueue(link_4);
                            pageHtml = (String) HttpRequestTool.getRequest(link_4);
                            List<String> linksLevelFive = WaterHtmlParse.getHrefWithA(pageHtml);
                            pushRootUrlQueue(linksLevelFive);
                        }
                    }
                }
            }
        }
    }
}
