package com.water.ce.work;

import com.water.ce.crawl.director.CrawlDirector;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.web.model.dto.CrawlingTask;
import com.xpush.serialization.protobuf.ProtoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/17.
 */
public class TaskTest {

    public void test() {
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        CrawlingTask crawlingTask = new CrawlingTask().setTaskId("123").setDatas(crawlerArticleUrlList);
        CrawlDirector.submitTask(QueueClientHelper.FETCH_ARTICLE_QUEUE, crawlingTask);
    }
}
