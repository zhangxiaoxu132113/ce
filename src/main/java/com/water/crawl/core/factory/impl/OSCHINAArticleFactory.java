package com.water.crawl.core.factory.impl;

import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.utils.HttpRequestTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.UUID;

/**
 * Created by zhangmiaojie on 2017/3/2.
 */
public class OSCHINAArticleFactory implements IArticleFactory {

    @Override
    public ITArticle createArticle(Document doc, String decryptUrl) {
        ITArticle article = new ITArticle();
        String url = "https://my.oschina.net/alchemystar/blog/843832";
        String html = (String) HttpRequestTool.getRequest(url, false);
        Document document = Jsoup.parse(html);
        String title = document.select(".blog-heading .title").get(0).ownText();
        String author = document.select("").get(0).ownText();
        String content = document.select("").get(0).ownText();
        String description = document.select("").get(0).ownText();
        String reference = document.select("").get(0).ownText();

        article.setId(UUID.randomUUID().toString());
        article.setAuthor(author);
        article.setTitle(title);
        article.setCategory("IBM");
        article.setContent(content);
        article.setDescription(description);
        article.setReference(reference);
//        article.setReleaseTime(date);
        article.setCreateOn(System.currentTimeMillis());
        article.setDescryptUrl(decryptUrl);
        return null;
    }

    @Override
    public void printExecuteInfo() {

    }

    public static void main(String[] args) {
        ITArticle article = new ITArticle();
        String url = "https://my.oschina.net/alchemystar/blog/843832";
        String html = (String) HttpRequestTool.getRequest(url, false);
        Document document = Jsoup.parse(html);
        String title = document.select(".blog-heading .title").get(0).ownText();
        String author = document.select(".name a").get(0).ownText();
        String content = document.select("#blogBody").get(0).html();
//        String description = document.select("").get(0).ownText();
//        String reference = document.select("").get(0).ownText();

        article.setId(UUID.randomUUID().toString());
        article.setAuthor(author);
        article.setTitle(title);
        article.setCategory("IBM");
        article.setContent(content);
//        article.setDescription(description);
//        article.setReference(reference);
//        article.setReleaseTime(date);
        article.setCreateOn(System.currentTimeMillis());
        article.setDescryptUrl(url);
    }


}
