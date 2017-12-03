package com.water.ce.web.service.impl;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.Constant;
import com.water.ce.utils.QueueClientHelper;
import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.dto.CrawlerArticleUrl;
import com.water.ce.web.service.IBMCrawlingArticleService;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.model.TbCeFetchUrl;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by mrwater on 2017/11/19.
 */
@Service("ibmCrawlingArticleService")
public class IBMCrawlingArticleServiceImpl extends CrawlingArticleServiceImpl implements IBMCrawlingArticleService {
    public static String TOPIC_CATEGORY_URL = "https://www.ibm.com/developerworks/cn/topics/";
    private static final String WEB_SITE = "www.ibm.com";
    private static final String MODULE = "article";
    private static Log log = LogFactory.getLog(IBMCrawlingArticleServiceImpl.class);

    @Resource
    private TbCeFetchUrlMapper fetchUrlMapper;

    /**
     * 获取当前页面的所有文章链接
     *
     * @param page
     * @return List<String>
     */
    private List<String> getAllArticleLinkWithPage(String url, int page) {
        log.info("fetch article link ===>page is " + page + "url is" + url);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("start", String.valueOf(page + (page - 1) * 100));
        paramMap.put("end", String.valueOf(page * 100));
        String result = (String) com.water.ce.utils.http.HttpRequestTool.postRequest(url, paramMap, false);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        List<String> links = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.parse(result);
            Elements eles = doc.select("table.ibm-data-table tbody");
            if (eles == null || eles.size() <= 0) {
                return null;
            }
            Element articleListEle = eles.get(0);
            if (articleListEle != null) {
                Elements linkEles = articleListEle.getElementsByTag("a");
                for (Element linkEle : linkEles) {
                    links.add(linkEle.attr("abs:href"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return links;
    }

    /**
     * 获取IBM开发者社区所有模块的分类
     *
     * @return Set<String>
     */
    private Set<String> getIBMArticleCategoryUrl() {
        Set<String> linkList = null;
        String result = (String) HttpRequestTool.getRequest(TOPIC_CATEGORY_URL);
        if (StringUtils.isNotBlank(result)) {
            Document doc = Jsoup.parse(result);
            Elements links = doc.select("#ibm-content-main .ibm-columns").get(0).select("a[href]");
            if (links != null && links.size() > 0) {
                linkList = new HashSet<String>();
                List<String> tmpListLink = new ArrayList<String>();
                for (Element element : links) {
                    tmpListLink.add(element.attr("href"));
                    System.out.println(element.attr("href"));
                }

                for (String url : tmpListLink) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("开始请求 ===========================" + url);
                    result = (String) HttpRequestTool.getRequest(url, false);
                    if (StringUtils.isNotBlank(result)) doc = Jsoup.parse(result);
                    if (doc.select(".ibm-ind-link a").size() > 0) {
                        Element element = doc.select(".ibm-ind-link a").get(0);
                        if (element.text() != null) {
                            String requestUrl = element.attr("abs:href");
                            if (requestUrl == null) continue;
                            if (requestUrl.startsWith("http://www.ibm.com/")) {
                                if (requestUrl.contains(".jsp")) {
                                    requestUrl = requestUrl.substring(0, requestUrl.indexOf(".jsp") + ".jsp".length());
                                    linkList.add(requestUrl);
                                    System.out.println(requestUrl);
                                }
                            }
                        }
                    }
                }
            }
        }
        return linkList;
    }

    @Override
    public void handle() {
        long startTime = System.currentTimeMillis();
        log.info("【IBM开发者社区】爬虫任务====================>开始");
        Set<String> articleCategoryUrls = this.getIBMArticleCategoryUrl();
        log.info("开始抓取IBM开发者社区各个模块的文章，" + articleCategoryUrls.size());

        String taskId = StringUtil.uuid();
        Date currentTime = new Date();
        TbCeFetchUrl fetchUrl;
        CrawlerArticleUrl crawlerArticleUrl;
        List<TbCeFetchUrl> fetchUrlList = new ArrayList<>();
        List<ProtoEntity> crawlerArticleUrlList = new ArrayList<>();
        for (String url : articleCategoryUrls) {
            log.info("开始抓取版块====================>" + url);
            for (int i = 1; true; i++) {//循环获取所有模块下每个页面的文章
                List<String> linkList = this.getAllArticleLinkWithPage(url, i);
                if (linkList == null || linkList.size() <= 0) {
                    log.info("版块抓取结束====================>" + url);
                    break;
                }
                for (String link : linkList) {
//                    crawlerArticleUrl = new CrawlerArticleUrl();
//                    crawlerArticleUrl.setUrl(link);
//                    crawlerArticleUrl.setWebSite(WEB_SITE);
//                    crawlerArticleUrl.setWebSiteModule(MODULE);
//                    crawlerArticleUrl.setCategory(Constant.ARTICLE_CATEGORY.BLOG.getIndex());
//                    crawlerArticleUrl.setModule(1);
//                    crawlerArticleUrlList.add(crawlerArticleUrl);

                    fetchUrl = new TbCeFetchUrl();
                    fetchUrl.setUrl(link);
                    fetchUrl.setOrigin(1);
                    fetchUrl.setCreateOn(currentTime);
                    fetchUrlList.add(fetchUrl);
                    System.out.println(fetchUrl);
                }
                if (fetchUrlList.size() > 1000) {
                    fetchUrlMapper.insertBatch(fetchUrlList);
                    fetchUrlList.clear();
                }
            }
        }

        fetchUrlMapper.insertBatch(fetchUrlList);
        long endTime = System.currentTimeMillis();
        //提交爬虫任务
        recordTask(taskId, "IBM开发者社区爬虫任务", crawlerArticleUrlList.size());
//        submitCrawlingTask(WEB_SITE, MODULE, taskId, crawlerArticleUrlList, QueueClientHelper.FETCH_ARTICLE_QUEUE);
        log.info("【IBM开发者社区】爬虫任务执行结束，一共耗时为" + (endTime - startTime));
    }
}
