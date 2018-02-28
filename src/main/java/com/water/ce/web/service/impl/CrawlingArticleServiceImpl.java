package com.water.ce.web.service.impl;

import com.water.ce.crawl.director.CrawlDirector;
import com.water.ce.utils.Constant;
import com.water.ce.utils.HtmlUtil;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.model.dto.CrawlingTask;
import com.water.ce.web.service.CrawlingArticleService;
import com.water.es.api.Service.IArticleService;
import com.water.uubook.dao.TbCeFetchArticleTaskMapper;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.dao.TbUbArticleMapper;
import com.water.uubook.model.*;
import com.water.uubook.model.dto.TbUbArticleDto;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by mrwater on 2017/11/19.
 */
public class CrawlingArticleServiceImpl implements CrawlingArticleService {
    @Resource
    private TbCeFetchArticleTaskMapper fetchArticleTaskMapper;

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    @Resource
    private TbUbArticleMapper articleMapper;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    @Override
    public Integer addArticle(TbUbArticle article) {
        return null;
    }

    private static Log logger = LogFactory.getLog(CrawlingArticleServiceImpl.class);

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

    public void fetchAllUrl(String webSite, String module, String taskName) {
        fetchAllUrl(webSite, module, null, taskName);
    }

    public void fetchAllUrl(String webSite, String module, Integer origin, String taskName) {
        String taskId = StringUtil.uuid();
        CrawlerArticleUrl crawlerArticleUrl;
        TbCeFetchUrlCriteria fetchUrlCriteria = new TbCeFetchUrlCriteria();
        if (origin != null) {
            TbCeFetchUrlCriteria.Criteria criteria = fetchUrlCriteria.createCriteria();
            criteria.andOriginEqualTo(origin);
        }
        List<TbCeFetchUrl> fetchUrlList = fetchUrlMapper.selectByExample(fetchUrlCriteria);
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        for (TbCeFetchUrl fetchUrl : fetchUrlList) {
            crawlerArticleUrl = new CrawlerArticleUrl();
            crawlerArticleUrl.setUrl(fetchUrl.getUrl());
            crawlerArticleUrl.setWebSite(webSite);
            crawlerArticleUrl.setWebSiteModule(module);
            crawlerArticleUrl.setModule(0);
            crawlerArticleUrl.setOrigin(origin);
            crawlerArticleUrlList.add(crawlerArticleUrl);
        }
        recordTask(taskId, taskName, crawlerArticleUrlList.size());
        submitCrawlingTask(webSite, module, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
    }

    public void retryFetchUrl(String webSite, String module, String taskName) {
        String taskId = StringUtil.uuid();
        CrawlerArticleUrl crawlerArticleUrl;
        TbUbArticleCriteria articleCriteria = new TbUbArticleCriteria();
        TbUbArticleCriteria.Criteria criteria = articleCriteria.createCriteria();
        criteria.andTitleEqualTo("");
        List<TbUbArticle> articleList = articleMapper.selectByExample(articleCriteria);
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        for (TbUbArticle article : articleList) {
            crawlerArticleUrl = new CrawlerArticleUrl();
            crawlerArticleUrl.setUrl(article.getDescryptUrl());
            crawlerArticleUrl.setWebSite(webSite);
            crawlerArticleUrl.setWebSiteModule(module);
            crawlerArticleUrl.setModule(1);
            crawlerArticleUrlList.add(crawlerArticleUrl);
        }
        recordTask(taskId, taskName, crawlerArticleUrlList.size());
        submitCrawlingTask(webSite, module, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);

    }

    public void importArticle2Es() {
        Map<String, Object> queryMap = new HashMap<>();
        Integer id = 0;
        queryMap.put("count", 100);
        double processValue = 0.0;
        int allValue = articleMapper.countByExample(null);
        logger.info("开始处理任务，数据总量为" + allValue);
        while (true) {
            queryMap.put("id", id);
            List<TbUbArticle> articleList = articleMapper.getArticle(queryMap);
            for (TbUbArticle article : articleList) {
                String description = article.getDescription();
                String content = HtmlUtil.Html2Text(article.getContent());
                if (StringUtils.isBlank(description)) {// 如果description内容为空，则设置description的内容
                    if (content.length() >= 255) {
                        description = content.substring(0, 255);
                    } else {
                        description = content;
                    }
                    article.setDescription(description);
                    articleMapper.updateByPrimaryKeySelective(article);
                }

                article.setContent(content);
                com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
                BeanUtils.copyProperties(article, esArticle);
                esArticleService.addArticle(esArticle);
                id = article.getId();
            }

            processValue += 100.0;
            logger.info("已处理:" + processValue + ",当前进度=" + ((processValue / allValue) * 100) + "%");
        }
    }

    @Override
    public void addFetchUrlList(List<TbCeFetchUrl> fetchUrlList, String articleLink, int origin, Date currentTime) {
        TbCeFetchUrl fetchUrl = new TbCeFetchUrl();
        fetchUrl.setUrl(articleLink);
        fetchUrl.setOrigin(origin);
        fetchUrl.setCreateOn(currentTime);
        fetchUrlList.add(fetchUrl);
    }
}
