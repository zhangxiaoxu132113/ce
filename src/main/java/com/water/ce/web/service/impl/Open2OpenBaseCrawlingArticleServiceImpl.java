package com.water.ce.web.service.impl;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.Open2OpenCrawlingArticleService;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.dao.TbUbArticleMapper;
import com.water.uubook.model.TbCeFetchUrl;
import com.water.uubook.model.TbUbArticle;
import com.water.uubook.model.TbUbArticleCriteria;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 深度开源爬虫业务类
 * Created by mrwater on 2017/11/19.
 */
@Service("open2OpenCrawlingArticleService")
public class Open2OpenCrawlingArticleServiceImpl extends CrawlingArticleServiceImpl implements Open2OpenCrawlingArticleService {
    private static final String domain = "http://www.open-open.com";
    private static final String WEB_SITE = "open-open.com";
    private static final String MODULE = "article";

    private static Log log = LogFactory.getLog(Open2OpenCrawlingArticleServiceImpl.class);

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    @Resource
    private TbUbArticleMapper articleMapper;

    @Override
    public void handle() {
        long startTime = System.currentTimeMillis();
        log.info("【深度开源open2open.com】爬虫任务====================>开始");

        String taskId = StringUtil.uuid();
        TbCeFetchUrl tbCeFetchUrl;
        CrawlerArticleUrl crawlerArticleUrl;
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        List<TbCeFetchUrl> fetchUrlArrayList = new ArrayList<>();

        Document doc;
        String htmlPage;
        String realRequestUrl;
        Elements categoryList = this.getAllCategoryList();
        Date currentTime = new Date();
        for (Element cateogry : categoryList) {// 获取总的分类
            Elements sonCategories = cateogry.select(".item-list > .subitem > a");// 获取总的分类下的子分类
            for (Element sonCategory : sonCategories) {
                String fetchUrl = domain + sonCategory.attr("href");
                String category = sonCategory.html();
                String tmpFetchUrl = fetchUrl + "?pn=%s";
                int size = 0;
                int tryCount = 0;
                for (int pageNum = 0; ; pageNum++) {
                    realRequestUrl = pageNum != 0 ? String.format(tmpFetchUrl, pageNum) : fetchUrl;
                    htmlPage = (String) HttpRequestTool.getRequest(realRequestUrl);
                    doc = Jsoup.parse(htmlPage);
                    Elements topicList = doc.select(".container > .row > .col-md-8 > .list > li");
                    if (topicList == null || topicList.size() <= 0) {
                        tryCount++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (tryCount == 3) {
                            break;
                        } else {
                            continue;
                        }

                    }
                    tryCount = 0;
                    for (Element topicEle : topicList) {
                        String articleLink = domain + topicEle.select(".cont > a").attr("href");
                        crawlerArticleUrl = new CrawlerArticleUrl();
                        crawlerArticleUrl.setUrl(articleLink);
                        crawlerArticleUrl.setWebSite(WEB_SITE);
                        crawlerArticleUrl.setWebSiteModule(MODULE);
                        crawlerArticleUrl.setModule(1);
                        crawlerArticleUrlList.add(crawlerArticleUrl);

                        tbCeFetchUrl = new TbCeFetchUrl();
                        tbCeFetchUrl.setUrl(articleLink);
                        tbCeFetchUrl.setOrigin(0);
                        tbCeFetchUrl.setCreateOn(currentTime);
                        fetchUrlArrayList.add(tbCeFetchUrl);
                    }
                    if (fetchUrlArrayList.size() > 1000) {
                        fetchUrlMapper.insertBatch(fetchUrlArrayList);
                        fetchUrlArrayList.clear();
                    }
                    size++;
                }
                log.info("抓取分类 = " + category + ", 链接" + fetchUrl + ", pagesize = " + size);
            }
        }
        fetchUrlMapper.insertBatch(fetchUrlArrayList);
        long endTime = System.currentTimeMillis();
        //提交爬虫任务
        recordTask(taskId, "【深度开源open2open.com】爬虫任务", crawlerArticleUrlList.size());
        submitCrawlingTask(WEB_SITE, MODULE, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
        log.info("【深度开源open2open.com】爬虫任务执行结束，一共耗时为" + (endTime - startTime));
    }

    /**
     * 获取所有分类下的url地址
     *
     * @return
     */
    private Elements getAllCategoryList() {
        String url = "http://www.open-open.com/lib/list/401?pn=1";
        Document doc;
        String htmlPage;
        htmlPage = (String) HttpRequestTool.getRequest(url, false);
        doc = Jsoup.parse(htmlPage);
        Element allList = doc.select(".all-sort-list").get(0);
        Elements categoryList = allList.select(".item");
        return categoryList;
    }
}
