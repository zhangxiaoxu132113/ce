package com.water.ce.web;

import com.water.ce.cache.CacheManager;
import com.water.ce.crawl.director.CrawlDirector;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.model.dto.CrawlingTask;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/17.
 */
@RestController
public class TestController {
    @Resource
    private CacheManager cacheManager;

    @RequestMapping(value = "/test")
    public String test() {
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        CrawlerArticleUrl crawlerArticleUrl = new CrawlerArticleUrl();
        crawlerArticleUrl.setCategory(1);
        crawlerArticleUrl.setUrl("http://localhost:8080/wxqyh/jsp/default/login/login_result_success.jsp");
        crawlerArticleUrl.setUrlId("232321231");
        crawlerArticleUrlList.add(crawlerArticleUrl);
        CrawlingTask crawlingTask = new CrawlingTask().setTaskId("123").setDatas(crawlerArticleUrlList);
        CrawlDirector.submitTask(QueueClientHelper.FETCH_ARTICLE_QUEUE, crawlingTask);

        cacheManager.set("ee", "ee");
        return "dd";
    }
}
