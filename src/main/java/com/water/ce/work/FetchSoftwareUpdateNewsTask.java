package com.water.ce.work;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.water.ce.crawl.CrawlAction;
import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.Constant;
import com.water.ce.utils.lang.StringUtil;
import com.water.uubook.model.TbUbArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Created by mrwater on 2017/11/16.
 */
public class FetchSoftwareUpdateNewsTask {

    private Gson gson = new Gson();

    public void fetchArticle() {
        CrawlAction crawlAction = new CrawlAction("IBM", "TbUbArticle") {
            @Override
            public void action(JsonObject obj, Object data) {
                String json = obj.toString();
                Type type = new TypeToken<TbUbArticle>() {
                }.getType();
                TbUbArticle article = gson.fromJson(json, type);
                if (article != null) {
                    article.setCategory(0);
                    article.setViewHits(0);
                    article.setContent(handleContent(article.getContent(), this.getUrl()));
                    article.setModule(Constant.ARTICLE_MODULE.BLOG.getIndex());
                    if (article.getCreateOn() == null) {
                        article.setCreateOn(System.currentTimeMillis());
                    }
                }
            }
        };
    }

    private String handleContent(String content, String url) {
        Document doc = Jsoup.parse(content);
        Elements eles = doc.select("img");
        for (Element ele : eles) {
            String imgSrc = ele.attr("src");
            String newImgSrc = url.substring(0, url.lastIndexOf("/")+1) + imgSrc;
            ele.attr("src", newImgSrc);
        }

        return doc.toString();
    }
}
