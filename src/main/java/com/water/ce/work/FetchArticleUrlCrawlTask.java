package com.water.ce.work;

import com.water.ce.cache.CacheManager;
import com.water.ce.crawl.WaterHtmlParse;
import com.water.ce.utils.http.HttpRequestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.water.ce.utils.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 1，当程序启动的时候，会传入一个要抓取的网站的根url路径，保存到fetching_root_urls队列中
 * 2，调用start方法开始执行名程序
 * 3，while循环，每次判断fetching_root_urls队列中是否有数据，有的话就取出一条数据作为根 url开始爬取数据
 * 4，爬虫执行的深度为三层，执行到第四层的时候，不再执行下去，将第四层的url连接放到fetching_root_urls对列中
 * 放到fetching_root_urls队列之前先从fetching_root_urls_record判断这个root是否已经爬取过了
 * 然后也要判断是否超过了队列的长度（后面考虑，定义一个定点长度的队列）
 * 5, 每次爬取url之前会判断该url是否是在我们要爬取的网站服务（或域名），如果不是，则跳过，避免爬取其他网站的数据
 * 然后判断这个链接是否已经访问多了，如果访问过了则不处理，如果没有访问过则访问该链接，并解析该链接是是否是我们想要的文章连接
 * 如果是的话，则保存到fetching_article_urls这个队列中。
 * 直到根url下的三层抓取完，则在重复上面的操作，直到把整个网站给榨干。
 * <p>
 * Created by zhangmiaojie on 2017/3/30.
 */
public class FetchArticleUrlCrawlTask implements Runnable {
    private final static String ROOT_URL = "http://blog.csdn.net/u012099869/article/details/51179226";
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
        if (isEmpty) {
            cacheManager.del(RedisKey.Prefix.FETCHING_ROOT_URLS);
        }
        pushUrl(ROOT_URL, RedisKey.Prefix.FETCHING_ROOT_URLS, true);
    }

    /**
     * 将跟节点放到队列
     *
     * @param links 跟节点
     */
    public void pushRootUrlQueue(List<String> links) {
        if (cacheManager.llen(RedisKey.Prefix.FETCHING_ROOT_URLS) < 100000) {
            for (String link : links) {
                if (!parseUrlAndPutQueue(link)) {
                    pushUrl(link, RedisKey.Prefix.FETCHING_ROOT_URLS, false);
                }
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
            if (isCheck) {
                if (cacheManager.sadd(RedisKey.Prefix.FETCHING_URL_RECORDS, url) != 0) {
                    cacheManager.lpush(queueName, url);
                }
            } else {
                cacheManager.lpush(queueName, url);
            }
        }
    }

    /**
     * 解析过滤url，将文章的链接放到队列
     * http://blog.csdn.net/pelick/article/details/8331798
     */
    public boolean parseUrlAndPutQueue(String url) {
        boolean result = false;
        if (StringUtils.isNotBlank(url)) {
//            log.info("解析过滤url = " + url);
            if (url.contains("blog.csdn.net") && url.contains("article")) {
                pushUrl(url, RedisKey.Prefix.FETCHing_ARTICLE_URLS, false);
                result = true;
            }
        }
        return result;
    }

    @Override
    public void run() {
        String rootUrl;
        String pageHtml;
        while (cacheManager.llen(RedisKey.Prefix.FETCHING_ROOT_URLS) > 0) {
            //访问root
            rootUrl = cacheManager.lpop(RedisKey.Prefix.FETCHING_ROOT_URLS);
            if (StringUtils.isBlank(rootUrl)) continue;
            pageHtml = (String) HttpRequestTool.getRequest(rootUrl, false);
            List<String> links = WaterHtmlParse.getHrefWithA(pageHtml);
            for (String link_1 : links) {// 获取root下面说的所有的url连接 -- 第一层
                pageHtml = getAndParseRequestUrl(link_1);
                if (StringUtils.isBlank(pageHtml)) continue;
                List<String> linksLevelTwo = WaterHtmlParse.getHrefWithA(pageHtml);
                for (String link_2 : linksLevelTwo) {// 遍历第一层的连接，开始抓取第二层的url连接 -- 第二层
                    pageHtml = getAndParseRequestUrl(link_2);
                    if (StringUtils.isBlank(pageHtml)) continue;
                    List<String> linksLevelThree = WaterHtmlParse.getHrefWithA(pageHtml);
                    for (String link_3 : linksLevelThree) {//遍历第二层的连接，开始抓取第三层的url连接 -- 第三层
                        pageHtml = getAndParseRequestUrl(link_3);
                        if (StringUtils.isBlank(pageHtml)) continue;
                        List<String> linksLevelFour = WaterHtmlParse.getHrefWithA(pageHtml);
                        for (String link_4 : linksLevelFour) {//遍历第三层的连接，将四层的url连接放到一个根url节点的队列中 -- 终止循环
                            pageHtml = getAndParseRequestUrl(link_4);
                            if (StringUtils.isBlank(pageHtml)) continue;
                            List<String> linksLevelFive = WaterHtmlParse.getHrefWithA(pageHtml);
                            pushRootUrlQueue(linksLevelFive);
                            try {
                                Thread.sleep(1000 * 1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
//                        log.warn("------------------------------------------------------------");
                    }
                }
            }
//            log.info("root over ---------------------------------------------------------------");
        }
        log.info("任务执行完毕！");
    }

    public String getAndParseRequestUrl(String url) {
        if (StringUtils.isNotBlank(url) && url.contains("csdn")) {
            if (!cacheManager.sismember(RedisKey.Prefix.FETCHING_URL_RECORDS, url)) { //如果抓取过，则跳过
                parseUrlAndPutQueue(url);
                cacheManager.sadd(RedisKey.Prefix.FETCHING_URL_RECORDS, url);
                return (String) HttpRequestTool.getRequest(url);
            }
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
