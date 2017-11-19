package com.water.ce.web;

import com.google.protobuf.InvalidProtocolBufferException;
import com.water.ce.cache.CacheManager;
import com.water.ce.crawl.director.CrawlDirector;
import com.water.ce.crawl.handle.QuartHanlder;
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
    public String test() throws InvalidProtocolBufferException, IllegalAccessException, InstantiationException {
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        CrawlerArticleUrl crawlerArticleUrl = new CrawlerArticleUrl();
        crawlerArticleUrl.setCategory(1);
        crawlerArticleUrl.setUrl("http://localhost:8080/wxqyh/jsp/default/login/login_result_success.jsp");
        crawlerArticleUrl.setUrlId("232321231");
        crawlerArticleUrlList.add(crawlerArticleUrl);

        CrawlingTask crawlingTask = new CrawlingTask();
        crawlingTask.setTaskId("123");
        crawlingTask.setDatas(crawlerArticleUrlList);
        crawlingTask.setWebSite("www.ibm.com");
        crawlingTask.setModule("article");
        CrawlDirector.submitTask(QueueClientHelper.FETCH_ARTICLE_QUEUE, crawlingTask);


        QuartHanlder hanlder = new QuartHanlder();
        hanlder.handle(cacheManager);
        return "dd";
    }
}
