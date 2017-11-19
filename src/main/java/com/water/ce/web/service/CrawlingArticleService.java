package com.water.ce.web.service;


import com.water.uubook.model.Article;
import com.xpush.serialization.protobuf.ProtoEntity;

import java.util.List;

public interface CrawlingArticleService {

    /**
     * 添加文章
     * @param  article 文章对象
     * @return Integer
     */
    Integer addArticle(Article article);

    /**
     * 处理爬虫定时任务
     */
    void submitCrawlingTask(String webSite, String module, String taskId, List<ProtoEntity> protoEntityList, String queueName);

    void handle();

    void recordTask(String taskId, String taskName, int urlNum);
}