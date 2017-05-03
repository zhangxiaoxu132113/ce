package com.water.crawl.db.service.article.impl;

import com.water.crawl.db.service.article.IOSCHINAArticleService;
import com.water.crawl.utils.HttpRequestTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangmiaojie on 2017/3/2.
 */
@Service
public class OSCHINAArticleServiceImpl implements IOSCHINAArticleService {
    public static String CATEGORY_URL = "https://www.oschina.net/blog";

    public Set<String> getAllCategory() {
        Set<String> linkSet = new HashSet<>();
        String html = (String) HttpRequestTool.getRequest(CATEGORY_URL, getHeaderMap(), false);
        Document doc = Jsoup.parse(html);

        Elements categoryElements = doc.select(".blog-nav li a");
        for (Element element : categoryElements) {
            String link = element.attr("href");
            System.out.println(link);
            linkSet.add(link);
        }
        return linkSet;
    }

    public Map<String, String> getHeaderMap() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        return headers;
    }
}
