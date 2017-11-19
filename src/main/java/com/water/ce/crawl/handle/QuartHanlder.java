package com.water.ce.crawl.handle;

import com.google.protobuf.InvalidProtocolBufferException;
import com.water.ce.cache.CacheManager;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.model.dto.CrawlingTask;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrwater on 2017/11/18.
 */
@Service
public class QuartHanlder {

    @Resource
    private CacheManager cacheManager;

    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    public void handle(CacheManager cacheManager) throws InvalidProtocolBufferException, IllegalAccessException, InstantiationException {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    String queue_task = QueueClientHelper.FETCH_ARTICLE_QUEUE + "_task";
                    if (cacheManager.llen(queue_task) <= 0) {
                        return;
                    }
                    byte[] taskByte = cacheManager.getqueueheader(queue_task.getBytes());
                    CrawlingTask crawlingTask = new CrawlingTask();
                    ProtoEntity.parseFrom(crawlingTask, taskByte);

                    if (cacheManager.llen(QueueClientHelper.FETCH_ARTICLE_QUEUE) == 0) {
                        return;
                    }
                    byte[] urlByte = cacheManager.lpop(QueueClientHelper.FETCH_ARTICLE_QUEUE.getBytes());

                    CrawlerArticleUrl crawlerArticleUrl = new CrawlerArticleUrl();
                    ProtoEntity.parseFrom(crawlerArticleUrl, urlByte);

                    System.out.println("开始抓取文章链接:" + crawlerArticleUrl.getUrl());
                    System.out.println(crawlingTask.getTaskId());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }, 10L, 60L, TimeUnit.SECONDS);


    }
}
