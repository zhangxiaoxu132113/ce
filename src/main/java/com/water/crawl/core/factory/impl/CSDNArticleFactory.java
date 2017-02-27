package com.water.crawl.core.factory.impl;


import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.db.model.ITArticle;
import org.jsoup.nodes.Document;

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
}
