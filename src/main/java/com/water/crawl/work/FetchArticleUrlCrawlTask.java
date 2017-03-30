package com.water.crawl.work;

import com.water.crawl.core.WaterHtmlParse;
import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.utils.HttpRequestTool;
import com.water.crawl.utils.RedisKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangmiaojie on 2017/3/30.
 */
public class FetchArticleUrlCrawlTask implements Runnable {
    private final static String ROOT_URL = "http://blog.csdn.net/pelick/article/details/8331798";
    private Log log = LogFactory.getLog(FetchArticleUrlCrawlTask.class);

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
        pushUrl(ROOT_URL, RedisKey.Prefix.FETCHING_ROOT_URLS,false);
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
                pushUrl(link, RedisKey.Prefix.FETCHING_ROOT_URLS,false);
            }
        }
    }

    /**
     * 校验url没有重复，将url放到给定的队列
     *
     * @param url       连接
     * @param queueName 队列名称
     */
    public void pushUrl(String url, String queueName, boolean isCheck) {
        if (StringUtils.isNotBlank(url) && url.contains("csdn")) {
            ShardedJedis jedis = cacheManager.getShardedJedis();
            if (isCheck) {
                if (jedis.sadd(RedisKey.Prefix.FETCHING_URL_RECORDS, url) != 0) {
                    jedis.lpush(queueName, url);
                }
            } else {
                jedis.lpush(queueName, url);
            }
        }
    }

    /**
     * 解析过滤url，将文章的链接放到队列
     * http://blog.csdn.net/pelick/article/details/8331798
     */
    public void parseUrlAndPutQueue(String url) {
        if (StringUtils.isNotBlank(url)) {
            log.info("解析过滤url = " + url);
            if (url.contains("blog.csdn.net") && url.contains("article")) {
                pushUrl(url, RedisKey.Prefix.FETCHing_ARTICLE_URLS,true);
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
                pageHtml = getRequestUrl(link_1);
                if (StringUtils.isBlank(pageHtml)) continue;
                List<String> linksLevelTwo = WaterHtmlParse.getHrefWithA(pageHtml);

                for (String link_2 : linksLevelTwo) {// 遍历第一层的连接，开始抓取第二层的url连接 -- 第二层
                    parseUrlAndPutQueue(link_2);
                    pageHtml = getRequestUrl(link_2);
                    if (StringUtils.isBlank(pageHtml)) continue;
                    List<String> linksLevelThree = WaterHtmlParse.getHrefWithA(pageHtml);

                    for (String link_3 : linksLevelThree) {//遍历第二层的连接，开始抓取第三层的url连接 -- 第三层
                        parseUrlAndPutQueue(link_3);
                        pageHtml = getRequestUrl(link_3);
                        if (StringUtils.isBlank(pageHtml)) continue;
                        List<String> linksLevelFour = WaterHtmlParse.getHrefWithA(pageHtml);

                        for (String link_4 : linksLevelFour) {//遍历第三层的连接，将四层的url连接放到一个根url节点的队列中 -- 终止循环
                            parseUrlAndPutQueue(link_4);
                            pageHtml = getRequestUrl(link_4);
                            if (StringUtils.isBlank(pageHtml)) continue;
                            List<String> linksLevelFive = WaterHtmlParse.getHrefWithA(pageHtml);
                            pushRootUrlQueue(linksLevelFive);
                            log.warn("------------------------------------------------------------");
                            try {
                                Thread.sleep(1000 * 10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        log.info("任务执行完毕！");
    }

    public String getRequestUrl(String url) {
        if (StringUtils.isNotBlank(url) && url.contains("csdn")) {
            return (String) HttpRequestTool.getRequest(url);
        }
        return "";
    }

    public static void main(String[] args) {
        String url = "http://blog.csdn.net/blogdevteam/article/details/60961185";
        if (url.contains("blog.csdn.net") && url.contains("article")) {
            System.out.println("yes!");
        }
    }
}
