package com.water.crawl.db.service.article.impl;

import com.water.crawl.db.service.article.IBMArticleService;
import com.water.crawl.utils.HttpRequestTool;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangmiaojie on 2017/2/28.
 */
@Service("iBMArticleService")
public class IBMArticleServiceImpl extends ITArticleServiceImpl implements IBMArticleService {

    public static String TOPIC_CATEGORY_URL = "https://www.ibm.com/developerworks/cn/topics/";

    @Override
    public List<String> getAllArticleLink(String html) {
        List<String> links = new ArrayList<String>();
        Document doc = Jsoup.parse(html);
        Element articleListEle = doc.select("table.ibm-data-table tbody").get(0);
        if (articleListEle != null) {
            Elements linkEles = articleListEle.getElementsByTag("a");
            for (Element linkEle : linkEles) {
                links.add(linkEle.attr("abs:href"));
            }
        }
        return links;
    }


    @Override
    public Set<String> getIBMArticleCategoryUrl() {
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
                    result = (String) HttpRequestTool.getRequest(url);
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
}
