package com.water.ce.web.service.impl;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.Constant;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.OpenskillCrawlingArticleService;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.dao.TbUbArticleMapper;
import com.water.uubook.model.TbCeFetchUrl;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZMJ on 2017/12/23.
 */
public class OpenskillCrawlingArticleServiceImpl extends CrawlingArticleServiceImpl implements OpenskillCrawlingArticleService {

    private static final String WEB_SITE = "";
    private static final String WEB_MODULE = "";

    private static Log log = LogFactory.getLog(OpenskillCrawlingArticleServiceImpl.class);

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    @Resource
    private TbUbArticleMapper articleMapper;

    @Override
    public void handle() {
        long startTime = System.currentTimeMillis();
        log.info("【openskill.cn】爬虫任务====================>开始");
        String taskId = StringUtil.uuid();
        TbCeFetchUrl tbCeFetchUrl;
        CrawlerArticleUrl crawlerArticleUrl;
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        List<TbCeFetchUrl> fetchUrlArrayList = new ArrayList<>();

        Document doc;
        String html;
        String requestUrl;
        String baseUrl = "http://openskill.cn/article/page-%s";
        Date currentTime = new Date();
        for (int i=1;;i++) {
            requestUrl = String.format(baseUrl, i);
            html = (String) HttpRequestTool.getRequest(requestUrl, false);
            doc = Jsoup.parse(html);

            Elements articleEles = doc.select(".aw-question-content > h4 > a");
            if (articleEles.isEmpty()) {
                break;
            }
            for (Element articleEle : articleEles) {
                String articleLink = articleEle.attr("href");

                crawlerArticleUrl = new CrawlerArticleUrl();
                crawlerArticleUrl.setUrl(articleLink);
                crawlerArticleUrl.setWebSite(WEB_SITE);
                crawlerArticleUrl.setWebSiteModule(WEB_MODULE);
                crawlerArticleUrl.setModule(1);
                crawlerArticleUrlList.add(crawlerArticleUrl);

                tbCeFetchUrl = new TbCeFetchUrl();
                tbCeFetchUrl.setUrl(articleLink);
                tbCeFetchUrl.setOrigin(Constant.Article_ORIGIN.OPENSKILL.getIndex());
                tbCeFetchUrl.setCreateOn(currentTime);
                fetchUrlArrayList.add(tbCeFetchUrl);
            }
            if (fetchUrlArrayList.size() > 1000) {
                fetchUrlMapper.insertBatch(fetchUrlArrayList);
                fetchUrlArrayList.clear();
            }
        }
        fetchUrlMapper.insertBatch(fetchUrlArrayList);
        long endTime = System.currentTimeMillis();
        //提交爬虫任务
        recordTask(taskId, "【openskill.cn】爬虫任务", crawlerArticleUrlList.size());
        submitCrawlingTask(WEB_SITE, WEB_MODULE, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
        log.info("【openskill.cn】爬虫任务执行结束，一共耗时为" + (endTime - startTime));

    }
}
