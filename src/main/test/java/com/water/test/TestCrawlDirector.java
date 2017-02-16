package com.water.test;

import com.water.crawl.db.model.CrawlingTask;
import com.water.crawl.db.model.dto.CrawlKeyword;
import com.water.crawl.director.CrawlDirector;
import com.water.crawl.utils.QueueClientHelper;
import com.xpush.serialization.protobuf.ProtoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangmiaojie on 2017/2/14.
 */
public class TestCrawlDirector {

    public static void main(String[] args) {
        CrawlDirector.intialize("127.0.0.1:6379");
        CrawlDirector.registerWatchQueue(QueueClientHelper.QUEUE_FETCH_IMG_URL);

        List<ProtoEntity> list = new ArrayList<>();
        CrawlKeyword keyword = new CrawlKeyword();
        keyword.setId(UUID.randomUUID().toString());
        keyword.setKeyword("Zhang miaojie");
        keyword.setPv("23");
        list.add(keyword);

        CrawlKeyword keyword1 = new CrawlKeyword();
        keyword1.setId(UUID.randomUUID().toString());
        keyword1.setKeyword("Zhang wuji");
        keyword1.setPv("598");
        list.add(keyword);

        CrawlingTask task = new CrawlingTask().setTaskId(UUID.randomUUID().toString()).setDatas(list);
        CrawlDirector.submitTask(QueueClientHelper.QUEUE_FETCH_IMG_URL, task);
    }
}
