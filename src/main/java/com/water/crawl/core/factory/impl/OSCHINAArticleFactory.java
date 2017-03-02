package com.water.crawl.core.factory.impl;

import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.utils.HttpRequestTool;
import com.water.crawl.utils.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangmiaojie on 2017/3/2.
 */
public class OSCHINAArticleFactory implements IArticleFactory {

    @Override
    public ITArticle createArticle(Document doc, String decryptUrl) {
        return null;
    }

    @Override
    public void printExecuteInfo() {

    }

    public static void main(String[] args) {
        fetchArticle();
    }

    public static Set<String> getAllCategory() {
        Set<String> linkSet = new HashSet<>();
        String url = "https://www.oschina.net/blog";
        String html = (String) HttpRequestTool.getRequest(url, getHeaderMap(), false);
        Document doc = Jsoup.parse(html);

        Elements categoryElements = doc.select(".blog-nav li a");
        for (Element element : categoryElements) {
            String link = element.attr("href");
            System.out.println(link);
            linkSet.add(link);
        }
        return linkSet;
    }

    public static void fetchArticle() {
        String base_url = "https://www.oschina.net/blog";                                   //第一次进来不是ajax请求
        String ajax_url = "https://www.oschina.net/action/ajax/get_more_recommend_blog";    //第二次进来是ajax请求
        Set<String> linkSet = getAllCategory();
        for (String link : linkSet) {
            Map<String, String> queryParams = StringUtil.getParamWithUrl(link);
            String html = "";
            for (int i = 1; ; i++) {
                if (i == 1) {
                    html = (String) HttpRequestTool.postRequest(base_url, queryParams, getHeaderMap(), false);
                } else {
                    queryParams.put("q",String.valueOf(i));
                    html = (String) HttpRequestTool.postRequest(ajax_url, queryParams, getHeaderMap(), false);
                }

                Document doc = Jsoup.parse(html);
                Elements articleLinks = doc.select(".blog-title-link");
                for (Element articleLinkEle : articleLinks) {
                    String articleLink = articleLinkEle.attr("href");
                    System.out.println(articleLink);
                }
            }
        }
    }

    public static Map<String, String> getHeaderMap() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        return headers;
    }
}
