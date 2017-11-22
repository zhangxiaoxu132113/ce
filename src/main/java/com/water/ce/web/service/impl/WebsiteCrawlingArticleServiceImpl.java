package com.water.ce.web.service.impl;

import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.http.HttpRequestTool;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.WebsiteCrawlingArticleService;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/22.
 */
public class WebsiteCrawlingArticleServiceImpl extends BaseCrawlingArticleServiceImpl implements WebsiteCrawlingArticleService {
    private static Log log = LogFactory.getLog(WebsiteCrawlingArticleServiceImpl.class);

    public void fetchMQ() {
        long startTime = System.currentTimeMillis();
        log.info("【CSDB知识库】爬虫任务====================>开始");

        String taskId = StringUtil.uuid();
        CrawlerArticleUrl crawlerArticleUrl;
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();

        String d = "http://muxiulin.cn/archives/category/";
        String categories[] = new String[]{"rabbitmq", "kafka", "zeromq", "redis", "share"};
        String html;
        Document doc;

        for (String category : categories) {
            String fetchUrl = d + category;
            for (int i = 1; ; i++) {
                if (i != 1) {
                    fetchUrl += "/page/" + i;
                }
                html = (String) HttpRequestTool.getRequest(fetchUrl, false);
                doc = Jsoup.parse(html);

                Element mainEle = doc.select("#main").get(0);
                Elements articlesEle = mainEle.getElementsByTag("article");
                if (articlesEle == null || articlesEle.size() == 0) {
                    break;
                }
                for (Element ele : articlesEle) {
                    Element hrefEle = ele.select(".entry-title > a").get(0);
                    String articleLink = hrefEle.attr("href");
                    crawlerArticleUrl = new CrawlerArticleUrl(articleLink, "");
                    crawlerArticleUrlList.add(crawlerArticleUrl);
                }
            }
        }

        long endTime = System.currentTimeMillis();
        //提交爬虫任务
        recordTask(taskId, "【CSDB知识库】爬虫任务", crawlerArticleUrlList.size());
        submitCrawlingTask("muxiulin.cn", "article", taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
        log.info("【CSDB知识库】爬虫任务执行结束，一共耗时为" + (endTime - startTime));
    }
}
