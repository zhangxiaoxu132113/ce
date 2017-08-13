package com.water.crawl.core.factory.impl;


import com.water.crawl.core.factory.IArticleFactory;
import com.water.uubook.model.Article;
import com.water.uubook.model.ITArticle;
import org.jsoup.nodes.Document;

import java.util.UUID;

/**
 *
 * Created by zhangmiaojie on 2017/2/27.
 */
public class CSDNArticleFactory implements IArticleFactory {

    @Override
    public Article createArticle(Document doc, String decryptUrl) {
        Article article = new Article();
        try {
            String author = doc.select(".author a").get(0).ownText();
            String title = doc.select(".maincontent h1").get(0).ownText();
            String content = doc.select(".divtexts").get(0).html();
            article.setAuthor(author);
            article.setTitle(title);
            article.setContent(content);
//        article.setReleaseTime();
            article.setDescryptUrl(decryptUrl);
//            article.setCategory(Constant.ArticleCategory.CSDN.getName());
            article.setCreateOn(System.currentTimeMillis());
        } catch (Exception e) {
            article = null;
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public void printExecuteInfo() {

    }


    public static void main(String[] args) {
    }


}
