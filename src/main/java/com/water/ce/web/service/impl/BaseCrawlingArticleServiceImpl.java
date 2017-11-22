package com.water.ce.web.service.impl;

import com.water.ce.crawl.director.CrawlDirector;
import com.water.ce.utils.Constant;
import com.water.ce.web.model.dto.CrawlingTask;
import com.water.ce.web.service.BaseCrawlingArticleService;
import com.water.uubook.dao.TbCeFetchArticleTaskMapper;
import com.water.uubook.model.Article;
import com.water.uubook.model.TbCeFetchArticleTask;
import com.xpush.serialization.protobuf.ProtoEntity;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by mrwater on 2017/11/19.
 */
public class BaseCrawlingArticleServiceImpl implements BaseCrawlingArticleService {
    @Resource
    private TbCeFetchArticleTaskMapper fetchArticleTaskMapper;

    @Override
    public Integer addArticle(Article article) {
        return null;
    }

    @Override
    public void handle() {
    }

    @Override
    public void submitCrawlingTask(String webSite, String module, String taskId, List<ProtoEntity> protoEntityList, String queueName) {
        CrawlingTask crawlingTask = new CrawlingTask();
        crawlingTask.setTaskId(taskId).setWebSite(webSite).setModule(module);
        crawlingTask.setDatas(protoEntityList);
        CrawlDirector.submitTask(queueName, crawlingTask);
    }

    @Override
    public void recordTask(String taskId, String taskName, int urlNum) {
        Date currentTime = new Date();
        TbCeFetchArticleTask fetchArticleTask = new TbCeFetchArticleTask();
        fetchArticleTask.setId(taskId);
        fetchArticleTask.setName(taskName);
        fetchArticleTask.setStatus(Constant.TaskStatus.READY_STATUS);
        fetchArticleTask.setUrlNum(urlNum);
        fetchArticleTask.setCreateTime(currentTime);
        fetchArticleTask.setUpdateTime(currentTime);
        fetchArticleTaskMapper.insert(fetchArticleTask);
    }
}
