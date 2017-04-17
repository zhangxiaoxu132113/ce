package com.water.crawl.core.factory.impl;


import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.utils.Constant;
import org.jsoup.nodes.Document;

import java.util.UUID;

/**
 *
 * Created by zhangmiaojie on 2017/2/27.
 */
public class CSDNArticleFactory implements IArticleFactory {

    @Override
    public ITArticle createArticle(Document doc, String decryptUrl) {
        ITArticle article = new ITArticle();
        try {
            String author = doc.select(".author a").get(0).ownText();
            String title = doc.select(".maincontent h1").get(0).ownText();
            String content = doc.select(".divtexts").get(0).html();
            article.setId(UUID.randomUUID().toString());
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
