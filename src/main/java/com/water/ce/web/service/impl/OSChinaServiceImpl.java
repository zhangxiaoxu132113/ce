package com.water.ce.web.service.impl;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.OSChinaService;
import com.water.uubook.utils.Constants;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by mrwater on 2017/11/25.
 */
@Service("osChinaService")
public class OSChinaServiceImpl extends CrawlingArticleServiceImpl implements OSChinaService {
    private static final String BASE_URL = "https://www.oschina.net/blog";
    private static final String RECOMMEND_BLOG_URL = "https://www.oschina.net/action/ajax/get_more_recommend_blog";
    private static final String DAILY_BLOG_URL = "https://www.oschina.net/action/ajax/get_more_daily_blog";
    private static final String RECENT_BLOG_URL = "https://www.oschina.net/action/ajax/get_more_recent_blog";

    private static Log log = LogFactory.getLog(OSChinaServiceImpl.class);

    private static final String WEB_SITE = "www.oschina.net";
    private static final String MODULE = "article";

    private static final Set<String> fetchUrls = new HashSet<>();
    static  {
        fetchUrls.add(RECENT_BLOG_URL);
        fetchUrls.add(RECOMMEND_BLOG_URL);
        fetchUrls.add(DAILY_BLOG_URL);
    }

    @Override
    public void handle() {
        long startTime = System.currentTimeMillis();
        log.info("【开源中国】爬虫任务====================>开始");

        CrawlerArticleUrl crawlerArticleUrl;
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();

        Document doc = null;
        Elements articleLinks = null;
        Set<String> linkSet = this.getAllCategory();
        for (String link : linkSet) {
            log.info("fetch --> " + link);
            Map<String, String> queryParams = StringUtil.getParamWithUrl(link);
            for (String fetchLink : fetchUrls) {
                for (int i = 1; ; i++) {
                    queryParams.put("p", String.valueOf(i));
                    String html = (String) HttpRequestTool.postRequest(fetchLink, queryParams,  false);
                    if (StringUtils.isBlank(html)) {
                        continue;
                    }
                    doc = Jsoup.parse(html);
                    articleLinks = doc.select(".blog-title-link");
                    if (articleLinks == null || articleLinks.size() <= 0) {
                        break;
                    }
                    for (Element articleLinkEle : articleLinks) {
                        String articleLink = articleLinkEle.attr("href");
                        crawlerArticleUrl = new CrawlerArticleUrl(articleLink);
                        crawlerArticleUrl.setUrl(articleLink);
                        crawlerArticleUrl.setModule(Constants.ARTICLE_MODULE.BLOG);
                        crawlerArticleUrlList.add(crawlerArticleUrl);
                    }
                }
            }

            //提交爬虫任务
            String taskId = StringUtil.uuid();
            recordTask(taskId, "【开源中国】爬虫任务", crawlerArticleUrlList.size());
            submitCrawlingTask(WEB_SITE, MODULE, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
            log.info("爬虫任务" + taskId + "已经提交到爬虫队列");
            crawlerArticleUrlList.clear();
        }
        long endTime = System.currentTimeMillis();
        log.info("【开源中国】爬虫任务执行结束，一共耗时为" + (endTime - startTime));
    }

    private Set<String> getAllCategory() {
        Set<String> linkSet = new HashSet<>();
        String html = (String) HttpRequestTool.getRequest(BASE_URL, false);
        Document doc = Jsoup.parse(html);
        Elements categoryElements = doc.select(".blog-nav li a");
        for (Element element : categoryElements) {
            String link = element.attr("href");
            System.out.println(link);
            linkSet.add(link);
        }
        linkSet.remove("https://www.oschina.net/blog");
        return linkSet;
    }
}