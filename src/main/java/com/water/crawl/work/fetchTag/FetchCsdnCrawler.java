package com.water.crawl.work.fetchTag;

import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.utils.http.HttpRequestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Resource;

/**
 * Created by zhangmiaojie on 2017/8/15.
 */
public class FetchCsdnCrawler {
    public final static Integer MAX_DEPTH = 3;
    public final static String QUEUE_ROOT_URL = "queue_root_url";
    public final static String QUEUE_ARTICLE_URL = "queue_article_url";
    private Log logger = LogFactory.getLog(FetchCsdnCrawler.class);

    @Resource
    private CacheManager cacheManager;

    public void execute() {
        String htmlContent = "";
        Document doc = null;
        boolean isEnd = false;
        int depth = 1;
        String rootUrl =  cacheManager.lpop(QUEUE_ROOT_URL);
        while (!isEnd) {
            depthCapture(rootUrl, depth);
            Long queueLen = cacheManager.llen(QUEUE_ROOT_URL);
        }
    }

    /**
     * 递归抓取数据
     * @param url
     * @param depth
     */
    public void depthCapture(String url, int depth) {
        if (depth <= MAX_DEPTH) {
            depth++;
            String htmlContent = "";
            Document doc = null;
            htmlContent = (String) HttpRequestTool.getRequest(url);
            if (StringUtils.isNotBlank(htmlContent)) {
                doc = Jsoup.parse(htmlContent);
                Elements hrefEles = doc.select("a");

                for (Element hrefEle : hrefEles) {
                    String requestUrl = hrefEle.attr("href");
                    if (requestUrl.contains("csdn")) {
                        cacheManager.lpush(QUEUE_ROOT_URL, requestUrl);
                        if (requestUrl.contains("/article/details")) cacheManager.lpush(QUEUE_ARTICLE_URL, requestUrl);
                        depthCapture(requestUrl, depth);
                    }
                }
            }
        }
    }
}
