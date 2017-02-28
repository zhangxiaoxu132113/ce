package com.water.crawl.core.factory.impl;


import com.alibaba.fastjson.JSONObject;
import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.utils.HttpRequestTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by zhangmiaojie on 2017/2/27.
 */
public class CSDNArticleFactory implements IArticleFactory {
    @Override
    public ITArticle createArticle(Document doc, String decryptUrl) {
        return null;
    }

    @Override
    public void printExecuteInfo() {

    }



    public static void main(String[] args) {
        String url = "http://lib.csdn.net/bases";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("type","all");
        for (int i=1;;i++) {
            queryParams.put("page",String.valueOf(i));
            String html = (String) HttpRequestTool.postRequest(url, queryParams, false);
            System.out.println(html);
            JSONObject jsonObject = JSONObject.parseObject(html);
            html = (String) jsonObject.get("html");
            Document document = Jsoup.parse(html);
            Elements elements = document.select(".whitebk");
            if (elements == null || elements.size() <=0){
                System.out.println("已经获取了所有知识库分类");
                break;
            }
            for (Element element : elements) {
                String name = element.select(".title").get(0).ownText();
                String link = element.select(".title").get(0).attr("href");
                System.out.println(name + " : " + link);
            }
        }
    }
}
